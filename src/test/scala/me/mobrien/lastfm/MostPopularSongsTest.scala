package me.mobrien.lastfm

import org.scalatest._

class MostPopularSongsTest extends FlatSpec with Matchers {

  it should "calculate the 100 most played songs" in {
    val results = MostPopularSongs.query()
    results.size shouldEqual 100
    results.head shouldEqual SongWithNumberOfPlays("The Postal Service", "Such Great Heights", 3992)
    results.last shouldEqual SongWithNumberOfPlays("Snow Patrol", "Run", 2048)
  }
}
