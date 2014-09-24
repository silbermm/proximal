name := """IT5041-web"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  cache,
  jdbc,
  "com.typesafe.slick" %% "slick" % "2.1.0",
  "com.typesafe.play" %% "play-slick" % "0.8.0",
  "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
  "org.scalatestplus" %% "play" % "1.1.0" % "test"
)
