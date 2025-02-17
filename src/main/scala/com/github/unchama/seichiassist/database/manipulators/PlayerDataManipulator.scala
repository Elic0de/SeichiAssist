package com.github.unchama.seichiassist.database.manipulators

import cats.data.EitherT
import cats.effect.IO
import com.github.unchama.contextualexecutor.builder.ResponseEffectOrResult
import com.github.unchama.seichiassist.SeichiAssist
import com.github.unchama.seichiassist.data.RankData
import com.github.unchama.seichiassist.data.player.PlayerData
import com.github.unchama.seichiassist.database.{DatabaseConstants, DatabaseGateway}
import com.github.unchama.seichiassist.task.{CoolDownTask, PlayerDataLoading}
import com.github.unchama.seichiassist.util.BukkitSerialization
import com.github.unchama.targetedeffect.TargetedEffect
import com.github.unchama.targetedeffect.commandsender.MessageEffect
import com.github.unchama.util.ActionStatus
import org.bukkit.Bukkit
import org.bukkit.ChatColor._
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import scalikejdbc.{DB, scalikejdbcSQLInterpolationImplicitDef}

import java.sql.SQLException
import java.text.SimpleDateFormat
import java.util.{Calendar, UUID}
import scala.collection.mutable

class PlayerDataManipulator(private val gateway: DatabaseGateway) {

  import com.github.unchama.util.syntax.ResultSetSyntax._

  private val plugin = SeichiAssist.instance

  private val tableReference: String =
    s"${gateway.databaseName}.${DatabaseConstants.PLAYERDATA_TABLENAME}"

  // 投票特典配布時の処理(p_givenvoteの値の更新もココ)
  def compareVotePoint(player: Player, playerdata: PlayerData): Int = {
    ifCoolDownDoneThenGet(player, playerdata) {
      val struuid = playerdata.uuid.toString

      var p_vote = 0
      var p_givenvote = 0

      var command = s"select p_vote,p_givenvote from $tableReference where uuid = '$struuid'"
      try {
        gateway.executeQuery(command).recordIteration { lrs =>
          p_vote = lrs.getInt("p_vote")
          p_givenvote = lrs.getInt("p_givenvote")
        }
      } catch {
        case e: SQLException =>
          println("sqlクエリの実行に失敗しました。以下にエラーを表示します")
          e.printStackTrace()
          player.sendMessage(RED.toString + "投票特典の受け取りに失敗しました")
          return 0
      }

      // 比較して差があればその差の値を返す(同時にp_givenvoteも更新しておく)
      if (p_vote > p_givenvote) {
        command = ("update " + tableReference
          + " set p_givenvote = " + p_vote
          + s" where uuid = '$struuid'")
        if (gateway.executeUpdate(command) == ActionStatus.Fail) {
          player.sendMessage(RED.toString + "投票特典の受け取りに失敗しました")
          return 0
        }

        return p_vote - p_givenvote
      }
      player.sendMessage(YELLOW.toString + "投票特典は全て受け取り済みのようです")
      0
    }
  }

  @inline private def ifCoolDownDoneThenGet(player: Player, playerdata: PlayerData)(
    supplier: => Int
  ): Int = {
    // 連打による負荷防止の為クールダウン処理
    if (!playerdata.votecooldownflag) {
      player.sendMessage(RED.toString + "しばらく待ってからやり直してください")
      return 0
    }
    new CoolDownTask(player, true, false).runTaskLater(plugin, 1200)

    supplier
  }

  // 最新のnumofsorryforbug値を返してmysqlのnumofsorrybug値を初期化する処理
  def givePlayerBug(player: Player): Int = {
    val uuid = player.getUniqueId.toString
    val numberToGrant = {
      val command = s"select numofsorryforbug from $tableReference where uuid = '$uuid'"
      val rawMaximum =
        try {
          gateway
            .executeQuery(command)
            .recordIteration {
              _.getInt("numofsorryforbug")
            }
            .head
        } catch {
          case e: Exception =>
            println("sqlクエリの実行に失敗しました。以下にエラーを表示します")
            e.printStackTrace()
            player.sendMessage(RED.toString + "ガチャ券の受け取りに失敗しました")
            return 0
        }

      Math.min(rawMaximum, 576)
    }

    {
      val updateCommand =
        s"update $tableReference " +
          s"set numofsorryforbug = numofsorryforbug - $numberToGrant where uuid = '$uuid'"

      if (gateway.executeUpdate(updateCommand) == ActionStatus.Fail) {
        player.sendMessage(RED.toString + "ガチャ券の受け取りに失敗しました")
        return 0
      }
    }

    numberToGrant
  }

  /**
   * 投票ポイントをインクリメントするメソッド。
   *
   * @param playerName
   *   プレーヤー名
   */
  def incrementVotePoint(playerName: String): Unit = {
    DB.localTx { implicit session =>
      sql"update playerdata set p_vote = p_vote + 1 where name = $playerName".update().apply()
    }
  }

  // 指定されたプレイヤーにガチャ券を送信する
  def addPlayerBug(playerName: String, num: Int): IO[ResponseEffectOrResult[Player, Unit]] = {
    val executeQuery = IO {
      import scalikejdbc._
      DB.localTx { implicit session =>
        sql"""update seichiassist set numofsorryforbug = numofsorryforbug + $num where name = $playerName"""
          .update()
          .apply()
      }
    }.void

    catchingDatabaseErrors(
      s"add admin-gacha for $playerName",
      EitherT.right(executeQuery).value
    )
  }

  def addChainVote(name: String): Unit =
    DB.localTx { implicit session =>
      val calendar = Calendar.getInstance()
      val dateFormat = new SimpleDateFormat("yyyy/MM/dd")

      val lastVote =
        sql"SELECT lastvote FROM playerdata WHERE name = $name"
          .map(_.string("lastvote"))
          .single()
          .apply()
          .getOrElse(dateFormat.format(calendar.getTime))

      sql"UPDATE playerdata SET lastvote = ${dateFormat.format(calendar.getTime)} WHERE name = $name"
        .update()
        .apply()

      val TodayDate = dateFormat.parse(dateFormat.format(calendar.getTime))
      val LastDate = dateFormat.parse(lastVote)
      val TodayLong = TodayDate.getTime
      val LastLong = LastDate.getTime

      val dateDiff = (TodayLong - LastLong) / (1000 * 60 * 60 * 24)
      val shouldIncrementChainVote = dateDiff <= 2L

      val newCount = if (shouldIncrementChainVote) {
        sql"""select chainvote from playerdata where name = $name"""
          .map(_.int("chainvote"))
          .first()
          .apply()
          .get + 1
      } else 1

      sql"""update playerdata set chainvote = $newCount where name = $name"""
    }

  // anniversary変更
  def setAnniversary(anniversary: Boolean, uuid: Option[UUID]): Boolean = {
    val command = s"UPDATE $tableReference SET anniversary = $anniversary" +
      uuid.map(u => s" WHERE uuid = '$u'").getOrElse("")

    if (gateway.executeUpdate(command) == ActionStatus.Fail) {
      Bukkit.getLogger.warning("sql failed. => setAnniversary")
      return false
    }
    true
  }

  def saveSharedInventory(
    player: Player,
    serializedInventory: String
  ): IO[ResponseEffectOrResult[Player, Unit]] = {
    val assertSharedInventoryBeEmpty: EitherT[IO, TargetedEffect[CommandSender], Unit] =
      for {
        sharedInventorySerialized <- EitherT(loadShareInv(player))
        _ <- EitherT.fromEither[IO] {
          if (sharedInventorySerialized != null && sharedInventorySerialized != "")
            Left(MessageEffect(s"${RED}既にアイテムが収納されています"))
          else
            Right(())
        }
      } yield ()

    val writeInventoryData = IO {
      // シリアル化されたインベントリデータを書き込む
      val updateCommand =
        s"UPDATE $tableReference SET shareinv = '$serializedInventory' WHERE uuid = '${player.getUniqueId}'"

      if (gateway.executeUpdate(updateCommand) == ActionStatus.Fail) {
        Bukkit.getLogger.warning(s"${player.getName} database failure.")
        Left(MessageEffect(s"${RED}アイテムの収納に失敗しました"))
      } else {
        Right(())
      }
    }

    for {
      _ <- EitherT(checkInventoryOperationCoolDown(player))
      _ <- assertSharedInventoryBeEmpty
      _ <- EitherT(writeInventoryData)
    } yield ()
  }.value

  def loadShareInv(player: Player): IO[ResponseEffectOrResult[CommandSender, String]] = {
    val loadInventoryData: IO[Either[Nothing, String]] = EitherT
      .right(IO {
        val command =
          s"SELECT shareinv FROM $tableReference WHERE uuid = '${player.getUniqueId}'"

        gateway.executeQuery(command).recordIteration(_.getString("shareinv")).headOption.get
      })
      .value

    for {
      _ <- EitherT(checkInventoryOperationCoolDown(player))
      serializedInventory <- EitherT(catchingDatabaseErrors(player.getName, loadInventoryData))
    } yield serializedInventory
  }.value

  private def checkInventoryOperationCoolDown(
    player: Player
  ): IO[Either[TargetedEffect[CommandSender], Unit]] = {
    val playerData = SeichiAssist.playermap(player.getUniqueId)
    IO {
      // 連打による負荷防止
      if (!playerData.shareinvcooldownflag)
        Left(MessageEffect(s"${RED}しばらく待ってからやり直してください"))
      else {
        new CoolDownTask(player, CoolDownTask.SHAREINV).runTaskLater(plugin, 200)
        Right(())
      }
    }
  }

  def clearShareInv(
    player: Player,
    playerdata: PlayerData
  ): IO[ResponseEffectOrResult[CommandSender, Unit]] = IO {
    val command = s"UPDATE $tableReference SET shareinv = '' WHERE uuid = '${playerdata.uuid}'"

    if (gateway.executeUpdate(command) == ActionStatus.Fail) {
      Bukkit.getLogger.warning(s"${player.getName} sql failed. => clearShareInv")
      Left(MessageEffect(s"${RED}アイテムのクリアに失敗しました"))
    } else
      Right(())
  }

  // TODO IO-nize
  def selectLeaversUUIDs(days: Int): List[UUID] = {
    val command = s"select name, uuid from $tableReference " +
      s"where ((lastquit <= date_sub(curdate(), interval $days day)) " +
      "or (lastquit is null)) and (name != '') and (uuid != '')"
    val uuidList = mutable.ArrayBuffer[UUID]()

    try {
      gateway.executeQuery(command).recordIteration { lrs =>
        try {
          uuidList += UUID.fromString(lrs.getString("uuid"))
        } catch {
          case _: IllegalArgumentException =>
            println(s"不適切なUUID: ${lrs.getString("name")}: ${lrs.getString("uuid")}")
        }
      }
    } catch {
      case e: SQLException =>
        println("sqlクエリの実行に失敗しました。以下にエラーを表示します")
        e.printStackTrace()
        return null
    }
    uuidList.toList
  }

  /**
   * 全ランキングリストの更新処理
   *
   * @return
   *   成否…true: 成功、false: 失敗 TODO この処理はDB上と通信を行う為非同期にすべき
   */
  def successRankingUpdate(): Boolean = {
    if (!successPlayTickRankingUpdate()) return false
    if (!successVoteRankingUpdate()) return false
    successAppleNumberRankingUpdate()
  }

  // ランキング表示用にプレイ時間のカラムだけ全員分引っ張る
  private def successPlayTickRankingUpdate(): Boolean = {
    val ranklist = mutable.ArrayBuffer[RankData]()
    val command = ("select name,playtick from " + tableReference
      + " order by playtick desc")
    try {
      gateway.executeQuery(command).recordIteration { lrs =>
        val rankdata = new RankData()
        rankdata.name = lrs.getString("name")
        rankdata.playtick = lrs.getInt("playtick")
        ranklist += rankdata
      }
    } catch {
      case e: SQLException =>
        println("sqlクエリの実行に失敗しました。以下にエラーを表示します")
        e.printStackTrace()
        return false
    }

    SeichiAssist.ranklist_playtick.clear()
    SeichiAssist.ranklist_playtick.addAll(ranklist)
    true
  }

  // ランキング表示用に投票数のカラムだけ全員分引っ張る
  private def successVoteRankingUpdate(): Boolean = {
    val ranklist = mutable.ArrayBuffer[RankData]()
    val command = ("select name,p_vote from " + tableReference
      + " order by p_vote desc")
    try {
      gateway.executeQuery(command).recordIteration { lrs =>
        val rankdata = new RankData()
        rankdata.name = lrs.getString("name")
        rankdata.p_vote = lrs.getInt("p_vote")
        ranklist += rankdata
      }
    } catch {
      case e: SQLException =>
        println("sqlクエリの実行に失敗しました。以下にエラーを表示します")
        e.printStackTrace()
        return false
    }

    SeichiAssist.ranklist_p_vote.clear()
    SeichiAssist.ranklist_p_vote.addAll(ranklist)
    true
  }

  // ランキング表示用に上げたりんご数のカラムだけ全員分引っ張る
  private def successAppleNumberRankingUpdate(): Boolean = {
    val ranklist = mutable.ArrayBuffer[RankData]()
    SeichiAssist.allplayergiveapplelong = 0
    val command = s"select name,p_apple from $tableReference order by p_apple desc"
    try {
      gateway.executeQuery(command).recordIteration { lrs =>
        val rankdata = new RankData()
        rankdata.name = lrs.getString("name")
        rankdata.p_apple = lrs.getInt("p_apple")
        ranklist += rankdata
        SeichiAssist.allplayergiveapplelong += rankdata.p_apple.toLong
      }
    } catch {
      case e: SQLException =>
        println("sqlクエリの実行に失敗しました。以下にエラーを表示します")
        e.printStackTrace()
        return false
    }

    SeichiAssist.ranklist_p_apple.clear()
    SeichiAssist.ranklist_p_apple.addAll(ranklist)
    true
  }

  // 全員に詫びガチャの配布
  def addAllPlayerBug(amount: Int): ActionStatus = {
    val command = s"update $tableReference set numofsorryforbug = numofsorryforbug + $amount"
    gateway.executeUpdate(command)
  }

  def selectPocketInventoryOf(
    uuid: UUID
  ): IO[ResponseEffectOrResult[CommandSender, Inventory]] = {
    val command = s"select inventory from $tableReference where uuid = '$uuid'"

    val executeQuery = IO {
      gateway
        .executeQuery(command)
        .recordIteration { lrs => BukkitSerialization.fromBase64(lrs.getString("inventory")) }
        .head
    }

    catchingDatabaseErrors(uuid.toString, EitherT.right(executeQuery).value)
  }

  private def catchingDatabaseErrors[R](
    targetName: String,
    program: IO[Either[TargetedEffect[CommandSender], R]]
  ): IO[Either[TargetedEffect[CommandSender], R]] = {
    program.attempt.flatMap {
      case Left(error) =>
        IO {
          Bukkit.getLogger.warning(s"database failure for $targetName.")
          error.printStackTrace()

          Left(MessageEffect(s"${RED}データベースアクセスに失敗しました。"))
        }
      case Right(result) => IO.pure(result)
    }
  }

  def inquireLastQuitOf(playerName: String): IO[TargetedEffect[CommandSender]] = {
    val fetchLastQuitData: IO[ResponseEffectOrResult[CommandSender, String]] = EitherT
      .right(IO {
        import scalikejdbc._
        DB.readOnly { implicit session =>
          sql"""select lastquit from playerdata where name = $playerName"""
            .map(rs => rs.string("lastquit"))
            .single()
            .apply()
        }.get
      })
      .value

    catchingDatabaseErrors(playerName, fetchLastQuitData).map {
      case Left(errorEffect) =>
        import com.github.unchama.generic.syntax._

        val messages = List(
          s"${RED}最終ログアウト日時の照会に失敗しました。",
          s"${RED}プレイヤー名が変更されていないか確認してください。",
          s"${RED}プレイヤー名が正しいのにこのエラーが出る場合、最終ログイン時間が古い可能性があります。"
        )

        errorEffect.followedBy(MessageEffect(messages))
      case Right(lastQuit) =>
        MessageEffect(s"${playerName}の最終ログアウト日時：$lastQuit")
    }
  }

  def loadPlayerData(playerUUID: UUID, playerName: String): PlayerData = {
    val databaseGateway = SeichiAssist.databaseGateway
    val table = DatabaseConstants.PLAYERDATA_TABLENAME
    val db = SeichiAssist.seichiAssistConfig.getDB

    // sqlコネクションチェック
    databaseGateway.ensureConnection()

    // 同ステートメントだとmysqlの処理がバッティングした時に止まってしまうので別ステートメントを作成する
    val stmt = databaseGateway.con.createStatement()

    val stringUuid: String = playerUUID.toString.toLowerCase()

    // uuidがsqlデータ内に存在するか検索
    val count = {
      val command = s"select count(*) as count from $db.$table where uuid = '$stringUuid'"

      stmt.executeQuery(command).recordIteration(_.getInt("count")).headOption
    }

    count match {
      case Some(0) =>
        // uuidが存在しない時
        SeichiAssist.instance.getLogger.info(s"$YELLOW${playerName}は完全初見です。プレイヤーデータを作成します")

        // 新しくuuidとnameを設定し行を作成
        val command =
          s"insert into $db.$table (name,uuid,loginflag) values('$playerName','$stringUuid','1')"
        stmt.executeUpdate(command)

        new PlayerData(playerUUID, playerName)
      case _ =>
        PlayerDataLoading.loadExistingPlayerData(playerUUID, playerName)
    }
  }
}
