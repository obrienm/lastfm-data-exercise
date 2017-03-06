package me.mobrien.lastfm

import me.mobrien.lastfm.lib.{LastFMDataReader, Logging, SparkRunner}
import org.apache.spark.sql.Row
import org.apache.spark.sql.functions.col

/**
  * Create a list of the 100 most popular songs (artist and title) in the dataset, with the number of
  * times each was played.
  */
object MostPopularSongs extends App with SparkRunner with LastFMDataReader with Logging {

  override protected lazy val appName: String = "MostPopularSongs"

  run() { () =>
    val top100PlayedSongs = query()
    top100PlayedSongs.foreach(log.info)
  }

  def query(): List[SongWithNumberOfPlays] = {
    val sqlContext = sparkSession.newSession().sqlContext
    val dataFrame = getLastFmDataFrame(sqlContext)

    val queryResults = dataFrame
      .groupBy("`artist-name`", "`track-name`")
      .count()
      .orderBy(col("count").desc)
      .limit(100)

    import sparkSession.implicits._

    import scala.collection.convert.wrapAll._

    queryResults.map(SongWithNumberOfPlays(_)).collectAsList().toList
  }
}

object SongWithNumberOfPlays {
  def apply(row: Row): SongWithNumberOfPlays = {
    val artistName = row.getAs[String]("artist-name")
    val trackName = row.getAs[String]("track-name")
    val playCount = row.getAs[Long]("count")
    SongWithNumberOfPlays(artistName, trackName, playCount)
  }
}
case class SongWithNumberOfPlays(artistName: String, trackName: String, playCount: Long) {
  override def toString: String = s"$artistName\t$trackName\t$playCount"
}