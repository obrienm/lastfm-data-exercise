package me.mobrien.lastfm

import org.scalatest._

class LongestSessionsTest extends FlatSpec with Matchers {

  it should "calculate the longest sessions" in {
    val results = LongestSessions.query()
    results.size shouldEqual 39542
    val sessions = results.groupBy(r => r.syntheticSessionId)
    sessions.size shouldEqual 10

    sessions.getOrElse("user_000949151", Nil).size shouldEqual 5360
    sessions.getOrElse("user_00054475", Nil).size shouldEqual 5350
    sessions.getOrElse("user_000949139", Nil).size shouldEqual 4956
    sessions.getOrElse("user_000949559", Nil).size shouldEqual 4705
    sessions.getOrElse("user_00099718", Nil).size shouldEqual 4357
    sessions.getOrElse("user_00054455", Nil).size shouldEqual 3651
    sessions.getOrElse("user_000949125", Nil).size shouldEqual 3077
    sessions.getOrElse("user_000949189", Nil).size shouldEqual 2834
    sessions.getOrElse("user_000949152", Nil).size shouldEqual 2652
    sessions.getOrElse("user_0002501285", Nil).size shouldEqual 2600
  }
}
