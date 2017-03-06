lazy val root = (project in file("."))
  .settings(
    name := "Last.fm exercise",
    scalaVersion := "2.11.8",
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % "2.1.0",
      "org.apache.spark" %% "spark-sql" % "2.1.0",
      "com.databricks" % "spark-csv_2.11" % "1.5.0",
      "org.scalatest" %% "scalatest" % "3.0.0" % "test"
    )
  )
