package me.mobrien.lastfm

import me.mobrien.lastfm.lib.{LastFMDataReader, Logging, SparkRunner}
import org.apache.spark.sql.Row
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.{col, _}
import org.apache.spark.sql.types.{IntegerType, LongType}
import java.sql.Timestamp


/**
  * Say we define a user’s “session” of Last.fm usage to be comprised of one or more songs played
  * by that user, where each song is started within 20 minutes of the previous song’s start time.
  * Create a list of the top 10 longest sessions, with the following information about each session:
  * userid, timestamp of first and last songs in the session, and the list of songs played in the
  * session (in order of play).
  */
object LongestSessions extends App with SparkRunner with LastFMDataReader with Logging {

  override protected lazy val appName: String = "LongestSessions"

  run() { () =>
    val top10LongestSessions = query()
    top10LongestSessions.foreach(log.info)
  }

  def query(): List[Session] = {
    val sqlContext = sparkSession.newSession().sqlContext
    val dataFrame = getLastFmDataFrame(sqlContext)

    val window = Window.partitionBy("userid").orderBy("timestamp")

    val twentyMinutesInSeconds = 20 * 60

    val isNewSession = col("timestamp").cast(LongType) - lag(col("timestamp").cast(LongType), 1, 0).over(window) >= twentyMinutesInSeconds
    val createSessionId = concat(col("userid"), sum("new-session-started").over(window))

    val tenLongestSessions =
      dataFrame
      .withColumn("new-session-started", isNewSession.cast(IntegerType))
      .withColumn("synthetic-session-id", createSessionId)
      .groupBy("synthetic-session-id")
      .agg(min("timestamp").alias("first-song-start"), max("timestamp").alias("last-song-start"))
      .withColumn("session-duration-seconds", col("last-song-start").cast(LongType) - col("first-song-start").cast(LongType))
      .orderBy(col("session-duration-seconds").desc)
      .limit(10)

    val completeTableWithSessionIds =
      dataFrame
        .withColumn("new-session-started", isNewSession.cast(IntegerType))
        .withColumn("synthetic-session-id", createSessionId)

    val queryResults = tenLongestSessions.
      join(completeTableWithSessionIds, "synthetic-session-id")
      .orderBy("session-duration-seconds", "timestamp")
      .select("userid", "first-song-start", "last-song-start", "track-name", "synthetic-session-id")

    import sparkSession.implicits._
    import scala.collection.convert.wrapAll._

    queryResults.map(Session(_)).collectAsList().toList
  }
}

object Session {
  def apply(row: Row): Session = {
    val userId = row.getAs[String]("userid")
    val firstSongStart = row.getAs[Timestamp]("first-song-start")
    val lastSongStart = row.getAs[Timestamp]("last-song-start")
    val trackName = row.getAs[String]("track-name")
    val syntheticSessionId = row.getAs[String]("synthetic-session-id")
    Session(userId, firstSongStart, lastSongStart, trackName, syntheticSessionId)
  }
}

case class Session(userId: String, firstSongStart: Timestamp, lastSongStart: Timestamp, trackName: String, syntheticSessionId: String) {
  override def toString: String = s"$userId\t$firstSongStart\t$lastSongStart\t$trackName\n$syntheticSessionId"
}
