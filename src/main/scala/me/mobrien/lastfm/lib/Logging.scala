package me.mobrien.lastfm.lib

import org.apache.log4j.{Level, Logger}

trait Logging {

  protected val log: Logger = Logger.getLogger(getClass)

  Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
}