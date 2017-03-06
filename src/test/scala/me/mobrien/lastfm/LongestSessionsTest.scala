package me.mobrien.lastfm

import org.scalatest._

class LongestSessionsTest extends FlatSpec with Matchers {

  it should "calculate the number of distinct songs played for each user" in {
    val results = LongestSessions.query()
    results.size shouldEqual 2159
    val sessions = results.groupBy(r => r.syntheticSessionId)
    sessions.size shouldEqual 10

    sessions.foreach {
      case (key, value) => println(s"$key ${value.size}")
    }

    sessions.getOrElse("user_000002581", Nil).size shouldEqual 303
    sessions.getOrElse("user_000002336", Nil).size shouldEqual 257
    sessions.getOrElse("user_0000031096", Nil).size shouldEqual 234
    sessions.getOrElse("user_000002273", Nil).size shouldEqual 224
    sessions.getOrElse("user_0000021646", Nil).size shouldEqual 222
    sessions.getOrElse("user_000003198", Nil).size shouldEqual 203
    sessions.getOrElse("user_000002272", Nil).size shouldEqual 185
    sessions.getOrElse("user_0000031576", Nil).size shouldEqual 183
    sessions.getOrElse("user_000002260", Nil).size shouldEqual 182
    sessions.getOrElse("user_0000011203", Nil).size shouldEqual 166
  }
}
