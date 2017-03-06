package me.mobrien.lastfm.lib

import org.apache.spark.sql.types.{StringType, StructField, StructType, TimestampType}
import org.apache.spark.sql.{DataFrame, SQLContext}

trait LastFMDataReader {

  private lazy val lastFmDataFilePath = this.getClass.getResource("/lastfm-dataset-1K/userid-timestamp-artid-artname-traid-traname.tsv").getPath

  protected def getLastFmDataFrame(sqlContext: SQLContext): DataFrame = {

    val schema = StructType(Array(
      StructField("userid", StringType),
      StructField("timestamp", TimestampType),
      StructField("musicbrainz-artist-id", StringType),
      StructField("artist-name", StringType),
      StructField("musicbrainz-track-id", StringType),
      StructField("track-name", StringType)
    ))

    sqlContext.read
      .format("com.databricks.spark.csv")
      .schema(schema)
      .option("delimiter", "\t")
      .load(lastFmDataFilePath)
  }

}
