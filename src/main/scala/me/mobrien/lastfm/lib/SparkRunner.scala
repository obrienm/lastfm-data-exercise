package me.mobrien.lastfm.lib

import org.apache.spark.sql.SparkSession

trait SparkRunner {

  protected val appName: String

  protected lazy val sparkSession: SparkSession = SparkSession
    .builder()
    // to running locally
    // if you get an exception about the hostname, find your hostname and put an entry in /etc/hosts, e.g
    // 127.0.0.1  my.localmachine
    .config("spark.master", "local")
    .appName(appName)
    .getOrCreate()

  def run()(functionToRun: () => Unit): Unit = {
    try {
      functionToRun()
    } finally {
      sparkSession.stop()
    }
  }
}