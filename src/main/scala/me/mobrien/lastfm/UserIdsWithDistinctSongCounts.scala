package me.mobrien.lastfm

import me.mobrien.lastfm.lib.{LastFMDataReader, Logging, SparkRunner}
import org.apache.spark.sql.Row
import org.apache.spark.sql.functions._


/**
  * Create a list of user IDs, along with the number of distinct songs each user has played.
  */
object UserIdsWithDistinctSongCounts extends App with SparkRunner with LastFMDataReader with Logging {

  override protected lazy val appName: String = "UserIdsWithSongCounts"

  run() { () =>
    val usersWithDistinctSongCount = query()
    usersWithDistinctSongCount.foreach(log.info)
    log.info(s"Total number of rows: ${usersWithDistinctSongCount.size}")
  }

  def query(): List[UserWithDistinctSongCount] = {
    val sqlContext = sparkSession.newSession().sqlContext
    val dataFrame = getLastFmDataFrame(sqlContext)

    val queryResults = dataFrame
      .groupBy("userid")
      .agg(countDistinct("track-name").alias("count"))
      .orderBy(col("count").desc)

    import sparkSession.implicits._
    import scala.collection.convert.wrapAll._

    queryResults.map(UserWithDistinctSongCount(_)).collectAsList().toList
  }

}

object UserWithDistinctSongCount {
  def apply(row: Row): UserWithDistinctSongCount = {
    val userId = row.getAs[String]("userid")
    val count = row.getAs[Long]("count")
    UserWithDistinctSongCount(userId, count)
  }
}
case class UserWithDistinctSongCount(userId: String, distinctSongCount: Long) {
  override def toString: String = s"$userId\t$distinctSongCount"
}