package com.github.unchama.seichiassist.listener

import cats.effect.{IO, SyncIO}
import com.github.unchama.generic.effect.concurrent.TryableFiber
import com.github.unchama.generic.effect.unsafe.EffectEnvironment
import com.github.unchama.menuinventory.router.CanOpen
import com.github.unchama.minecraft.actions.OnMinecraftServerThread
import com.github.unchama.seichiassist._
import com.github.unchama.seichiassist.data.GachaPrize
import com.github.unchama.seichiassist.effects.player.CommonSoundEffects
import com.github.unchama.seichiassist.menus.stickmenu.{FirstPage, StickMenu}
import com.github.unchama.seichiassist.seichiskill.ActiveSkill
import com.github.unchama.seichiassist.seichiskill.ActiveSkillRange.RemoteArea
import com.github.unchama.seichiassist.seichiskill.SeichiSkillUsageMode.Disabled
import com.github.unchama.seichiassist.seichiskill.assault.AssaultRoutine
import com.github.unchama.seichiassist.subsystems.mana.ManaApi
import com.github.unchama.seichiassist.task.CoolDownTask
import com.github.unchama.seichiassist.util._
import com.github.unchama.targetedeffect.player.FocusedSoundEffect
import com.github.unchama.util.bukkit.ItemStackUtil
import com.github.unchama.util.external.ExternalPlugins
import com.github.unchama.util.external.WorldGuardWrapper.isRegionOwner
import net.md_5.bungee.api.chat.{HoverEvent, TextComponent}
import org.bukkit.ChatColor._
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.{EventHandler, EventPriority, Listener}
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.material.{MaterialData, Openable}
import org.bukkit.{GameMode, Material, Sound}

import scala.collection.mutable

class PlayerClickListener(
  implicit effectEnvironment: EffectEnvironment,
  manaApi: ManaApi[IO, SyncIO, Player],
  ioCanOpenStickMenu: IO CanOpen FirstPage.type,
  ioOnMainThread: OnMinecraftServerThread[IO]
) extends Listener {

  import ManagedWorld._
  import com.github.unchama.generic.ContextCoercion._
  import com.github.unchama.seichiassist.concurrent.PluginExecutionContexts.{asyncShift, timer}
  import com.github.unchama.targetedeffect._
  import com.github.unchama.util.syntax._

  import scala.jdk.CollectionConverters._

  private val plugin = SeichiAssist.instance

  import plugin.activeSkillAvailability

  private val playerMap = SeichiAssist.playermap
  private val gachaDataList = SeichiAssist.gachadatalist

  // アクティブスキル処理
  @EventHandler
  def onPlayerActiveSkillEvent(event: PlayerInteractEvent): Unit = {
    val player = event.getPlayer
    val action = event.getAction

    val equipmentSlot = event.getHand

    if (equipmentSlot == null || equipmentSlot == EquipmentSlot.OFF_HAND) return
    if (player.isSneaking || player.getGameMode != GameMode.SURVIVAL || player.isFlying) return

    val playerData = playerMap(player.getUniqueId)
    val skillState = playerData.skillState.get.unsafeRunSync()

    if (skillState.usageMode == Disabled) return
    if (!player.getWorld.isSeichiSkillAllowed) return

    action match {
      case Action.LEFT_CLICK_BLOCK | Action.LEFT_CLICK_AIR =>
        if (skillState.assaultSkill.isEmpty) return
      case Action.RIGHT_CLICK_BLOCK | Action.RIGHT_CLICK_AIR =>
        if (skillState.assaultSkill.nonEmpty) return
      // クリック以外のInteractEventを無視する
      case Action.PHYSICAL => return
    }

    // クールダウンタイム中は処理を終了
    if (!activeSkillAvailability(player).get.unsafeRunSync()) {
      // SEを再生
      player.playSound(player.getLocation, Sound.BLOCK_DISPENSER_FAIL, 0.5f, 1f)
      return
    }

    if (MaterialSets.breakToolMaterials.contains(event.getMaterial)) {
      skillState.activeSkill match {
        case Some(ActiveSkill(_, _, RemoteArea(_), coolDownOption, _, _)) =>
          import cats.implicits._
          import com.github.unchama.concurrent.syntax._

          // クールダウン処理
          val coolDownTicks = coolDownOption.getOrElse(0)
          val soundEffectAfterCoolDown =
            if (coolDownTicks > 5)
              FocusedSoundEffect(Sound.ENTITY_ARROW_HIT_PLAYER, 0.5f, 0.1f)(player)
            else
              IO.unit

          val controlSkillAvailability =
            activeSkillAvailability(player).set(false).coerceTo[IO] >>
              IO.sleep(coolDownTicks.ticks) >>
              activeSkillAvailability(player).set(true).coerceTo[IO] >>
              soundEffectAfterCoolDown

          val arrowEffect = playerData.skillEffectState.selection.arrowEffect

          effectEnvironment.unsafeRunEffectAsync("スキルのクールダウンの状態を戻す", controlSkillAvailability)
          effectEnvironment.unsafeRunEffectAsync(
            "ArrowEffectを非同期で実行する",
            arrowEffect.run(player)
          )
        case _ =>
      }
    }
  }

  // プレイヤーが右クリックした時に実行(ガチャを引く部分の処理)
  @EventHandler
  def onPlayerRightClickGachaEvent(event: PlayerInteractEvent): Unit = {
    val player = event.getPlayer
    val playerData = playerMap(player.getUniqueId)

    // もしサバイバルでなければ処理を終了
    if (player.getGameMode != GameMode.SURVIVAL) return

    val clickedItemStack = event.getItem.ifNull {
      return
    }

    // ガチャ用の頭でなければ終了
    if (!ItemInformation.isGachaTicket(clickedItemStack)) return

    event.setCancelled(true)

    // 連打防止クールダウン処理
    if (!playerData.gachacooldownflag) return

    // 連打による負荷防止の為クールダウン処理
    new CoolDownTask(player, false, true).runTaskLater(plugin, 4)

    // オフハンドから実行された時処理を終了
    if (event.getHand == EquipmentSlot.OFF_HAND) return

    // ガチャデータが設定されていない場合
    if (gachaDataList.isEmpty) {
      player.sendMessage("ガチャが設定されていません")
      return
    }

    val action = event.getAction
    val clickedBlock = event.getClickedBlock.ifNull {
      return
    }

    /*
    AIRまたはBlockを右クリックしていない、または、Blockのときにチェストやトラップチェストをクリックしていれば処理を終了
    参照：https://github.com/GiganticMinecraft/SeichiAssist/issues/770
     */
    if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return
    if (
      action == Action.RIGHT_CLICK_BLOCK && (clickedBlock.getType == Material.CHEST || clickedBlock.getType == Material.TRAPPED_CHEST)
    ) return

    val count =
      if (player.isSneaking) {
        val amount = clickedItemStack.getAmount
        player.sendMessage(s"$AQUA${amount}回ガチャを回しました。")
        amount
      } else 1

    if (
      !InventoryOperations.removeItemfromPlayerInventory(
        player.getInventory,
        clickedItemStack,
        count
      )
    ) {
      player.sendMessage(RED.toString + "ガチャ券の数が不正です。")
      return
    }

    // 各自当たった個数を記録するための変数
    var gachaBigWin = 0
    var gachaWin = 0
    var gachaGTWin = 0

    val playerLevel = SeichiAssist
      .instance
      .breakCountSystem
      .api
      .seichiAmountDataRepository(player)
      .read
      .unsafeRunSync()
      .levelCorrespondingToExp
      .level

    (1 to count).foreach { _ =>
      // プレゼント用ガチャデータ作成
      val present = GachaPrize.runGacha()

      val probabilityOfItem = present.probability
      val givenItem = {
        val base = present.itemStack

        if (probabilityOfItem < 0.1) ItemStackUtil.appendOwnerInformation(player)(base)
        else base
      }

      /**
       *  メッセージ設定
       *  ①まずMineStackに入るか試す
       *  ②入らなかったらインベントリに直接入れる
       *  ③インベントリが満タンだったらドロップする
       */
      val additionalMessage =
        if (
          BreakUtil.tryAddItemIntoMineStack(
            player,
            present.itemStack
          ) && playerLevel >= SeichiAssist.seichiAssistConfig.getMineStacklevel(1)
        ) {
          // ...格納した！
          s"${AQUA}景品をマインスタックに収納しました。"
        } else {
          // スタックできないか、整地Lvがマインスタックの開放レベルに足りていないとき...
          // ...ドロップする
          if (!InventoryOperations.isPlayerInventoryFull(player)) {
            InventoryOperations.addItem(player, givenItem)
            ""
          } else {
            InventoryOperations.dropItem(player, givenItem)
            s"${AQUA}景品がドロップしました。"
          }
        }

      // 確率に応じてメッセージを送信
      if (probabilityOfItem < 0.001) {
        SendSoundEffect.sendEverySoundWithoutIgnore(Sound.ENTITY_ENDERDRAGON_DEATH, 0.5f, 2f)

        {
          playerData
            .settings
            .getBroadcastMutingSettings
            .flatMap(settings =>
              IO {
                if (!settings.shouldMuteMessages) {
                  player.playSound(player.getLocation, Sound.ENTITY_ENDERDRAGON_DEATH, 0.5f, 2f)
                }
              }
            )
        }.unsafeRunSync()

        val loreWithoutOwnerName = givenItem.getItemMeta.getLore.asScala.toList.filterNot {
          _ == s"§r§2所有者：${player.getName}"
        }

        val localizedEnchantmentList = givenItem.getItemMeta.getEnchants.asScala.toSeq.map {
          case (enchantment, level) =>
            s"$GRAY${EnchantNameToJapanese.getEnchantName(enchantment.getName, level)}"
        }

        import scala.util.chaining._
        val message =
          new TextComponent().tap { c =>
            import c._
            setText(s"$AQUA${givenItem.getItemMeta.getDisplayName}${GOLD}を引きました！おめでとうございます！")
            setHoverEvent {
              new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                Array(
                  new TextComponent(
                    s" ${givenItem.getItemMeta.getDisplayName}\n" +
                      ListFormatters.getDescFormat(localizedEnchantmentList.toList) +
                      ListFormatters.getDescFormat(loreWithoutOwnerName)
                  )
                )
              )
            }
          }

        player.sendMessage(s"${RED}おめでとう！！！！！Gigantic☆大当たり！$additionalMessage")
        player.spigot.sendMessage(message)
        SendMessageEffect.sendMessageToEveryone(
          s"$GOLD${player.getDisplayName}がガチャでGigantic☆大当たり！"
        )
        SendMessageEffect.sendMessageToEveryone(message)
        gachaGTWin += 1
      } else if (probabilityOfItem < 0.01) {
        player.playSound(player.getLocation, Sound.ENTITY_WITHER_SPAWN, 0.8f, 1f)
        if (count == 1) {
          player.sendMessage(s"${GOLD}おめでとう！！大当たり！$additionalMessage")
        }
        gachaBigWin += 1
      } else if (probabilityOfItem < 0.1) {
        if (count == 1) {
          player.sendMessage(s"${YELLOW}おめでとう！当たり！$additionalMessage")
        }
        gachaWin += 1
      } else {
        if (count == 1) {
          player.sendMessage(s"${WHITE}はずれ！また遊んでね！$additionalMessage")
        }
      }
    }

    val rewardDetailTexts = mutable.ArrayBuffer[String]()
    if (gachaWin > 0) rewardDetailTexts += s"${YELLOW}当たりが${gachaWin}個"
    if (gachaBigWin > 0) rewardDetailTexts += s"${GOLD}大当たりが${gachaBigWin}個"
    if (gachaGTWin > 0) rewardDetailTexts += s"${RED}Gigantic☆大当たりが${gachaGTWin}個"
    if (count != 1) {
      player.sendMessage(if (rewardDetailTexts.isEmpty) {
        s"${WHITE}はずれ！また遊んでね！"
      } else {
        s"${rewardDetailTexts.mkString(s"$GRAY,")}${GOLD}出ました！"
      })
    }

    player.playSound(player.getLocation, Sound.ENTITY_ARROW_HIT_PLAYER, 1f, 0.1f)
  }

  // スキル切り替えのイベント
  @EventHandler
  def onPlayerActiveSkillToggleEvent(event: PlayerInteractEvent): Unit = {
    val player = event.getPlayer
    val action = event.getAction

    val equipmentSlot = event.getHand

    val currentItem = player.getInventory.getItemInMainHand.getType
    if (currentItem == Material.STICK || currentItem == Material.SKULL_ITEM) return

    val playerData = playerMap(player.getUniqueId)
    val playerLevel = SeichiAssist
      .instance
      .breakCountSystem
      .api
      .seichiAmountDataRepository(player)
      .read
      .unsafeRunSync()
      .levelCorrespondingToExp
      .level

    if (playerLevel < SeichiAssist.seichiAssistConfig.getDualBreaklevel) return
    if (!player.getWorld.isSeichiSkillAllowed) return
    if (!activeSkillAvailability(player).get.unsafeRunSync()) return

    if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
      val hasToolInMainHand = MaterialSets.breakToolMaterials.contains(currentItem)

      if (
        action == Action.RIGHT_CLICK_BLOCK &&
        MaterialSets.cancelledMaterials.contains(event.getClickedBlock.getType)
      ) return

      val skillState = playerData.skillState.get.unsafeRunSync()

      if (equipmentSlot == EquipmentSlot.HAND && hasToolInMainHand) {
        // メインハンドで指定ツールを持っていた時の処理
        // スニークしていないかつアサルトタイプが選択されていない時処理を終了
        if (!player.isSneaking && skillState.assaultSkill.isEmpty) return

        // 設置をキャンセル
        event.setCancelled(true)

        skillState.activeSkill match {
          case Some(skill) =>
            val toggledMode = skillState.usageMode.nextMode(skill)

            playerData.skillState.set(skillState.copy(usageMode = toggledMode)).unsafeRunSync()
            player.sendMessage(s"$GOLD${skill.name}：${toggledMode.modeString(skill)}")
            player.playSound(player.getLocation, Sound.BLOCK_LEVER_CLICK, 1f, 1f)
          case None =>
        }
      } else if (equipmentSlot == EquipmentSlot.OFF_HAND) {
        skillState.assaultSkill match {
          case Some(skill) =>
            // オフハンドで指定ツールを持っていた時の処理

            event.setCancelled(true)

            import com.github.unchama.seichiassist.concurrent.PluginExecutionContexts.sleepAndRoutineContext

            SeichiAssist
              .instance
              .assaultSkillRoutines(player)
              .flipState(TryableFiber.start(AssaultRoutine.tryStart(player, skill)))
              .as(())
              .unsafeRunSync()

            player.playSound(player.getLocation, Sound.BLOCK_LEVER_CLICK, 1f, 1f)
          case None =>
        }
      }
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  def onPlayerOpenMenuWithSomething(event: PlayerInteractEvent): Unit = {
    val player = event.getPlayer
    val action = event.getAction
    val inventory = player.getInventory

    if (!(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) return

    if (
      inventory
        .getItemInMainHand
        .getType == Material.STICK && inventory.getItemInOffHand != null
    )
      event.setCancelled(true)
  }

  // 棒メニューを開くイベント
  @EventHandler
  def onPlayerMenuEvent(event: PlayerInteractEvent): Unit = {
    // プレイヤーを取得
    val player = event.getPlayer
    // プレイヤーが起こしたアクションを取得
    val action = event.getAction

    if (player.getInventory.getItemInMainHand.getType != Material.STICK) return

    // 右クリックの処理ではない
    if (!(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) return
    if (event.getHand == EquipmentSlot.OFF_HAND) return
    event.setCancelled(true)

    effectEnvironment.unsafeRunAsyncTargetedEffect(player)(
      SequentialEffect(
        CommonSoundEffects.menuTransitionFenceSound,
        ioCanOpenStickMenu.open(StickMenu.firstPage)
      ),
      "棒メニューの1ページ目を開く"
    )
  }

  // 頭の即時回収
  @EventHandler
  def onPlayerRightClickMineHeadEvent(e: PlayerInteractEvent): Unit = {

    val p = e.getPlayer
    val useItem = p.getInventory.getItemInMainHand
    // 専用アイテムを持っていない場合無視
    if (!ItemInformation.isMineHeadItem(useItem)) {
      return
    }

    val action = e.getAction
    // ブロックの左クリックじゃない場合無視
    if (action != Action.LEFT_CLICK_BLOCK) {
      return
    }

    val targetBlock = e.getClickedBlock
    // 頭じゃない場合無視
    if (targetBlock.getType != Material.SKULL) {
      return
    }

    // 壊せない場合無視
    if (!BreakUtil.canBreak(p, targetBlock)) {
      return
    }

    // インベントリに空がない場合無視
    if (InventoryOperations.isPlayerInventoryFull(p)) {
      p.sendMessage(RED.toString + "インベントリがいっぱいです")
      return
    }

    // 頭を付与
    ItemInformation.getSkullDataFromBlock(targetBlock) match {
      case Some(itemStack) => p.getInventory.addItem(itemStack)
      case None            =>
    }
    if (!ExternalPlugins.getCoreProtectWrapper.queueBlockRemoval(p, targetBlock)) {
      SeichiAssist
        .instance
        .getLogger
        .warning(
          s"Logging in skull break: Failed Location: ${targetBlock.getLocation}, Player:$p"
        )
    }
    // ブロックを空気で置き換える
    targetBlock.setType(Material.AIR)
    // 音を鳴らしておく
    p.playSound(p.getLocation, Sound.ENTITY_ITEM_PICKUP, 2.0f, 1.0f)
  }

  // 鉄のトラップドアを動力無しで開閉できるようにする処理
  // 参照：https://red.minecraftserver.jp/issues/8109
  @EventHandler
  def onPlayerRightClickIronTrapDoor(event: PlayerInteractEvent): Unit = {
    val clickedBlock = event.getClickedBlock
    if (clickedBlock == null) return

    if (!isRegionOwner(event.getPlayer, clickedBlock.getLocation)) return

    if (event.getHand == EquipmentSlot.OFF_HAND) return
    if (
      event.getAction != Action.RIGHT_CLICK_BLOCK || clickedBlock.getType != Material.IRON_TRAPDOOR
    ) return

    // TODO: 手に何も持っていない場合は機能するが、ブロックなどを持っている場合は機能しない（手に持っているものが設置できるもののときや弓矢は反応する）
    val blockState = clickedBlock.getState
    val materialData = blockState.getData.asInstanceOf[Openable]
    materialData.setOpen(!materialData.isOpen)
    blockState.setData(materialData.asInstanceOf[MaterialData])
    blockState.update()
  }
}
