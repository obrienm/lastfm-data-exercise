package me.mobrien.lastfm

import org.scalatest._

class UserIdsWithSongCountsTest extends FlatSpec with Matchers {

  it should "calculate the number of distinct songs played for each user" in {
    val results = UserIdsWithDistinctSongCounts.query()
    results.size shouldEqual 992
    results.head shouldEqual UserWithDistinctSongCount("user_000691", 55559)
    results.last shouldEqual UserWithDistinctSongCount("user_000332", 2)
  }
}
