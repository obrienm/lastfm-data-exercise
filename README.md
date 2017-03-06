## Last.fm data exercises

### Run

You'll need Java, sbt and spark installed on your machine. If you are using OSX you can install them via [homebrew](https://brew.sh/):

```shell
brew install sbt
brew install spark
```

Get the last.fm sample set:

```shell
mkdir -p src/main/resources
cd src/main/resources
wget http://mtg.upf.edu/static/datasets/last.fm/lastfm-dataset-1K.tar.gz
tar xvfj lastfm-dataset-1K.tar.gz
```

You can run the exercises and see their output using:

```shell
sbt "runMain me.mobrien.lastfm.UserIdsWithDistinctSongCounts"
sbt "runMain me.mobrien.lastfm.MostPopularSongs"
sbt "runMain me.mobrien.lastfm.LongestSessions"
```

Or you can just run the tests:

```shell
sbt "testOnly me.mobrien.lastfm.UserIdsWithDistinctSongCountsTest"
sbt "testOnly me.mobrien.lastfm.MostPopularSongsTest"
sbt "testOnly me.mobrien.lastfm.LongestSessionsTest"
```

### Alternative approaches

All three of these exercises could have been solved using Hive SQL. However, in each of the cases for [UserIdsWithDistinctSongCounts](https://github.com/obrienm/lastfm-data-exercise/blob/master/src/main/scala/me/mobrien/lastfm/UserIdsWithDistinctSongCounts.scala) and [MostPopularSongs](https://github.com/obrienm/lastfm-data-exercise/blob/master/src/main/scala/me/mobrien/lastfm/MostPopularSongs.scala) we could also have used a key/value store such as DynamoDB, or a document store such as elasticsearch, or even a traditional relational database to store these values, because all we are interested in is a counter. Every time a track is played we could just increment the counter.

[LongestSessions](https://github.com/obrienm/lastfm-data-exercise/blob/master/src/main/scala/me/mobrien/lastfm/LongestSessions.scala) is not as simple as the first two where we could just increment a counter. We could calculate the session id on the client side in the case of Last.fm but that feels like a fragile approach.
