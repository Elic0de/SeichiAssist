package com.github.unchama.buildassist

import cats.effect.{IO, SyncIO}
import com.github.unchama.buildassist.listener._
import com.github.unchama.buildassist.menu.BuildAssistMenuRouter
import com.github.unchama.datarepository.KeyedDataRepository
import com.github.unchama.generic.effect.concurrent.ReadOnlyRef
import com.github.unchama.generic.effect.unsafe.EffectEnvironment
import com.github.unchama.seichiassist.listener.BuildMainMenuOpener
import com.github.unchama.seichiassist.subsystems.buildcount.domain.playerdata.BuildAmountData
import com.github.unchama.seichiassist.subsystems.mana.ManaApi
import com.github.unchama.seichiassist.subsystems.managedfly.ManagedFlyApi
import com.github.unchama.seichiassist.{DefaultEffectEnvironment, subsystems}
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.{Bukkit, Material}

import java.util
import java.util.UUID
import scala.collection.mutable

class BuildAssist(plugin: Plugin)(
  implicit flyApi: ManagedFlyApi[SyncIO, Player],
  buildCountAPI: subsystems.buildcount.BuildCountAPI[IO, SyncIO, Player],
  manaApi: ManaApi[IO, SyncIO, Player]
) {

  // TODO この辺のフィールドを整理する

  /**
   * 永続化されない、プレーヤーのセッション内でのみ有効な一時データを管理するMap。 [[TemporaryDataInitializer]] によって初期化、削除される。
   */
  val temporaryData: mutable.HashMap[UUID, TemporaryMutableBuildAssistPlayerData] =
    mutable.HashMap()

  val buildAmountDataRepository
    : KeyedDataRepository[Player, ReadOnlyRef[SyncIO, BuildAmountData]] =
    buildCountAPI.playerBuildAmountRepository

  {
    BuildAssist.plugin = plugin
    BuildAssist.instance = this
  }

  def onEnable(): Unit = {
    implicit val menuRouter: BuildAssistMenuRouter[IO] = {
      import com.github.unchama.seichiassist.concurrent.PluginExecutionContexts.{
        layoutPreparationContext,
        onMainThread
      }

      BuildAssistMenuRouter.apply
    }

    // コンフィグ系の設定は全てConfig.javaに移動
    BuildAssist.config = new BuildAssistConfig(plugin)
    BuildAssist.config.loadConfig()

    import buildCountAPI._
    import menuRouter._

    implicit val effectEnvironment: EffectEnvironment = DefaultEffectEnvironment

    val listeners = List(
      new BuildMainMenuOpener(),
      new PlayerInventoryListener(),
      new TemporaryDataInitializer(this.temporaryData),
      new BlockLineUpTriggerListener[SyncIO],
      new TilingSkillTriggerListener[SyncIO]
    )

    listeners.foreach { listener =>
      Bukkit.getServer.getPluginManager.registerEvents(listener, plugin)
    }

    plugin.getLogger.info("BuildAssist is Enabled!")
  }

}

object BuildAssist {
  var instance: BuildAssist = _

  // 範囲設置ブロックの対象リスト
  val materiallist: java.util.Set[Material] = util
    .EnumSet
    .of(
      Material.STONE // 石
      ,
      Material.GRASS // 草
      ,
      Material.DIRT // 土
      ,
      Material.COBBLESTONE // 丸石
      ,
      Material.WOOD // 木
      ,
      Material.SAND // 砂
      ,
      Material.GRAVEL // 砂利
      ,
      Material.GOLD_ORE // 金鉱石
      ,
      Material.IRON_ORE // 鉄鉱石
      ,
      Material.COAL_ORE // 石炭鉱石
      ,
      Material.LOG // 原木
      ,
      Material.GLASS // ガラス
      ,
      Material.LAPIS_ORE // ラピス鉱石
      ,
      Material.LAPIS_BLOCK // ラピスB
      ,
      Material.SANDSTONE // 砂岩
      ,
      Material.WOOL // 羊毛
      ,
      Material.GOLD_BLOCK // 金B
      ,
      Material.IRON_BLOCK // 鉄B
      ,
      Material.BRICK // レンガB
      ,
      Material.BOOKSHELF // 本棚
      ,
      Material.MOSSY_COBBLESTONE // 苔石
      ,
      Material.OBSIDIAN // 黒曜石
      ,
      Material.DIAMOND_ORE // ダイヤ鉱石
      ,
      Material.DIAMOND_BLOCK // ダイヤB
      ,
      Material.REDSTONE_ORE // 赤鉱石
      ,
      Material.ICE // 氷
      ,
      Material.SNOW_BLOCK // 雪B
      ,
      Material.CLAY // 粘土B
      ,
      Material.NETHERRACK // ネザーラック
      ,
      Material.SOUL_SAND // ソウルサンド
      ,
      Material.GLOWSTONE // グロウストーン
      ,
      Material.STAINED_GLASS // 色付きガラス
      ,
      Material.SMOOTH_BRICK // 石レンガ
      ,
      Material.MYCEL // 菌糸
      ,
      Material.NETHER_BRICK // ネザーレンガ
      ,
      Material.ENDER_STONE // エンドストーン
      ,
      Material.EMERALD_ORE // エメ鉱石
      ,
      Material.EMERALD_BLOCK // エメB
      ,
      Material.COBBLE_WALL // 丸石の壁
      ,
      Material.QUARTZ_ORE // 水晶鉱石
      ,
      Material.QUARTZ_BLOCK // 水晶B
      ,
      Material.STAINED_CLAY // 色付き固焼き粘土
      ,
      Material.LOG_2 // 原木2
      ,
      Material.PRISMARINE // プリズマリン
      ,
      Material.SEA_LANTERN // シーランタン
      ,
      Material.HARD_CLAY // 固焼き粘土
      ,
      Material.COAL_BLOCK // 石炭B
      ,
      Material.PACKED_ICE // 氷塊
      ,
      Material.RED_SANDSTONE // 赤い砂岩
      ,
      Material.PURPUR_BLOCK // プルパーブ
      ,
      Material.PURPUR_PILLAR // 柱状プルパーブ
      ,
      Material.END_BRICKS // エンドレンガB
      ,
      Material.RED_NETHER_BRICK // 赤ネザーレンガB
      ,
      Material.BONE_BLOCK // 骨B
      ,
      Material.NETHER_WART_BLOCK // ネザーウォートB
      ,
      Material.CONCRETE // コンクリート
      ,
      Material.CONCRETE_POWDER // コンクリートパウダー
      ,
      Material.ACACIA_STAIRS,
      Material.ACACIA_FENCE,
      Material.ACACIA_FENCE_GATE,
      Material.BIRCH_WOOD_STAIRS,
      Material.BIRCH_FENCE,
      Material.BIRCH_FENCE_GATE,
      Material.BONE_BLOCK,
      Material.BOOKSHELF,
      Material.BRICK,
      Material.BRICK_STAIRS,
      Material.CACTUS,
      Material.CHEST,
      Material.CLAY_BRICK,
      Material.DARK_OAK_STAIRS,
      Material.DARK_OAK_FENCE,
      Material.DARK_OAK_FENCE_GATE,
      Material.END_BRICKS,
      Material.FURNACE,
      Material.GLOWSTONE,
      Material.HARD_CLAY,
      Material.JACK_O_LANTERN,
      Material.JUKEBOX,
      Material.JUNGLE_FENCE,
      Material.JUNGLE_FENCE_GATE,
      Material.JUNGLE_WOOD_STAIRS,
      Material.LADDER,
      Material.LEAVES,
      Material.LEAVES_2,
      Material.LOG,
      Material.LOG_2,
      Material.NETHER_BRICK,
      Material.NETHER_BRICK_STAIRS,
      Material.NETHER_WART_BLOCK,
      Material.RED_NETHER_BRICK,
      Material.OBSIDIAN,
      Material.PACKED_ICE,
      Material.PRISMARINE,
      Material.PUMPKIN,
      Material.PURPUR_BLOCK,
      Material.PURPUR_SLAB,
      Material.PURPUR_STAIRS,
      Material.PURPUR_PILLAR,
      Material.QUARTZ_BLOCK,
      Material.QUARTZ_STAIRS,
      Material.QUARTZ,
      Material.SANDSTONE,
      Material.SANDSTONE_STAIRS,
      Material.SEA_LANTERN,
      Material.SLIME_BLOCK,
      Material.SMOOTH_BRICK,
      Material.SMOOTH_STAIRS,
      Material.SNOW_BLOCK,
      Material.SPRUCE_FENCE,
      Material.SPRUCE_FENCE_GATE,
      Material.SPRUCE_WOOD_STAIRS,
      Material.FENCE,
      Material.FENCE_GATE,
      Material.STAINED_CLAY,
      Material.STAINED_GLASS,
      Material.STAINED_GLASS_PANE,
      Material.STEP,
      Material.STONE,
      Material.STONE_SLAB2,
      Material.THIN_GLASS,
      Material.TORCH,
      Material.WOOD,
      Material.WOOD_STAIRS,
      Material.WOOD_STEP,
      Material.WOOL,
      Material.CARPET,
      Material.WORKBENCH,
      // #1008
      Material.LEAVES,
      Material.LEAVES_2
    )

  // 直列設置ブロックの対象リスト
  val materiallist2: java.util.Set[Material] = util
    .EnumSet
    .of(
      Material.STONE // 石
      ,
      Material.GRASS // 草
      ,
      Material.DIRT // 土
      ,
      Material.COBBLESTONE // 丸石
      ,
      Material.WOOD // 木
      ,
      Material.SAND // 砂
      ,
      Material.GRAVEL // 砂利
      ,
      Material.GOLD_ORE // 金鉱石
      ,
      Material.IRON_ORE // 鉄鉱石
      ,
      Material.COAL_ORE // 石炭鉱石
      ,
      Material.LOG // 原木
      ,
      Material.GLASS // ガラス
      ,
      Material.LAPIS_ORE // ラピス鉱石
      ,
      Material.LAPIS_BLOCK // ラピスB
      ,
      Material.SANDSTONE // 砂岩
      ,
      Material.WOOL // 羊毛
      ,
      Material.GOLD_BLOCK // 金B
      ,
      Material.IRON_BLOCK // 鉄B
      ,
      Material.BRICK // レンガB
      ,
      Material.BOOKSHELF // 本棚
      ,
      Material.MOSSY_COBBLESTONE // 苔石
      ,
      Material.OBSIDIAN // 黒曜石
      ,
      Material.DIAMOND_ORE // ダイヤ鉱石
      ,
      Material.DIAMOND_BLOCK // ダイヤB
      ,
      Material.REDSTONE_ORE // 赤鉱石
      ,
      Material.ICE // 氷
      ,
      Material.SNOW_BLOCK // 雪B
      ,
      Material.CLAY // 粘土B
      ,
      Material.NETHERRACK // ネザーラック
      ,
      Material.SOUL_SAND // ソウルサンド
      ,
      Material.GLOWSTONE // グロウストーン
      ,
      Material.STAINED_GLASS // 色付きガラス
      ,
      Material.SMOOTH_BRICK // 石レンガ
      ,
      Material.MYCEL // 菌糸
      ,
      Material.NETHER_BRICK // ネザーレンガ
      ,
      Material.ENDER_STONE // エンドストーン
      ,
      Material.EMERALD_ORE // エメ鉱石
      ,
      Material.EMERALD_BLOCK // エメB
      ,
      Material.COBBLE_WALL // 丸石の壁
      ,
      Material.QUARTZ_ORE // 水晶鉱石
      ,
      Material.QUARTZ_BLOCK // 水晶B
      ,
      Material.STAINED_CLAY // 色付き固焼き粘土
      ,
      Material.LOG_2 // 原木2
      ,
      Material.PRISMARINE // プリズマリン
      ,
      Material.SEA_LANTERN // シーランタン
      ,
      Material.HARD_CLAY // 固焼き粘土
      ,
      Material.COAL_BLOCK // 石炭B
      ,
      Material.PACKED_ICE // 氷塊
      ,
      Material.RED_SANDSTONE // 赤い砂岩
      ,
      Material.PURPUR_BLOCK // プルパーブ
      ,
      Material.PURPUR_PILLAR // 柱状プルパーブ
      ,
      Material.END_BRICKS // エンドレンガB
      ,
      Material.RED_NETHER_BRICK // 赤ネザーレンガB
      ,
      Material.BONE_BLOCK // 骨B
      ,
      Material.FENCE // オークフェンス
      ,
      Material.IRON_FENCE // 鉄フェンス
      ,
      Material.THIN_GLASS // 板ガラス
      ,
      Material.NETHER_FENCE // ネザーフェンス
      ,
      Material.STAINED_GLASS_PANE // 色付き板ガラス
      ,
      Material.SLIME_BLOCK // スライムB
      ,
      Material.SPRUCE_FENCE // 松フェンス
      ,
      Material.BIRCH_FENCE // 白樺フェンス
      ,
      Material.JUNGLE_FENCE // ジャングルフェンス
      ,
      Material.DARK_OAK_FENCE // ダークオークフェンス
      ,
      Material.ACACIA_FENCE // アカシアフェンス
      ,
      Material.NETHER_WART_BLOCK // ネザーウォートB
      ,
      Material.CONCRETE // コンクリート
      ,
      Material.CONCRETE_POWDER // コンクリートパウダー
      ,
      Material.LEAVES,
      Material.LEAVES_2
    )

  // ハーフブロックとして扱うMaterial
  val material_slab2: java.util.Set[Material] = util
    .EnumSet
    .of(
      Material.STONE_SLAB2 // 赤砂岩
      ,
      Material.PURPUR_SLAB // プルパー
      ,
      Material.WOOD_STEP // 木
      ,
      Material.STEP // 石
    )

  val material_destruction: java.util.Set[Material] = util
    .EnumSet
    .of(
      Material.LONG_GRASS // 草
      ,
      Material.DEAD_BUSH // 枯れ木
      ,
      Material.YELLOW_FLOWER // タンポポ
      ,
      Material.RED_ROSE // 花9種
      ,
      Material.BROWN_MUSHROOM // きのこ
      ,
      Material.RED_MUSHROOM // 赤きのこ
      ,
      Material.TORCH // 松明
      ,
      Material.SNOW // 雪
      ,
      Material.DOUBLE_PLANT // 高い花、草
      ,
      Material.WATER // 水
      ,
      Material.STATIONARY_WATER // 水
      ,
      Material.LAVA // 溶岩
      ,
      Material.STATIONARY_LAVA // 溶岩
      ,
      Material.VINE // ツタ
    )

  var plugin: Plugin = _
  var config: BuildAssistConfig = _
  val line_up_str: Seq[String] = Seq("OFF", "上側", "下側")
  val line_up_step_str: Seq[String] = Seq("上側", "下側", "両方")
  val line_up_off_on_str: Seq[String] = Seq("OFF", "ON")
}
