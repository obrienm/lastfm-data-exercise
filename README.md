## Last.fm data exercises

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
sbt testOnly me.mobrien.lastfm.UserIdsWithDistinctSongCountsTest
sbt testOnly me.mobrien.lastfm.MostPopularSongsTest
sbt testOnly me.mobrien.lastfm.LongestSessions
```
