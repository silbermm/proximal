import com.typesafe.sbt.SbtGit._

name := """IT5041-web"""

javaOptions in Test += "-Dconfig.file=conf/test.conf"

versionWithGit

scalariformSettings

WebKeys.webTarget := target.value / "scala-web"

herokuAppName in Compile := "proximal"

artifactPath in PlayKeys.playPackageAssets := WebKeys.webTarget.value / (artifactPath in PlayKeys.playPackageAssets).value.getName

scalacOptions ++= Seq(
  "-feature",
  "-unchecked",
  "-deprecation",
  "-Ywarn-dead-code",
  "-Ywarn-unused-import",
  "-encoding", "UTF-8"
)

JsEngineKeys.engineType := JsEngineKeys.EngineType.Node

lazy val root = (project in file(".")).enablePlugins(PlayScala)

(testOptions in Test) += Tests.Argument(TestFrameworks.ScalaTest, "-u", "target/report")

ScoverageSbtPlugin.ScoverageKeys.coverageExcludedPackages := "<empty>;controllers.javascript;controllers.ref;securesocial.*;views.*;controllers.*;"

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  cache,
  jdbc,
  "com.typesafe.slick" %% "slick" % "2.1.0",
  "com.typesafe.play" %% "play-slick" % "0.8.0",
  "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
  "org.mindrot" % "jbcrypt" % "0.3m",
  "org.scalatestplus" %% "play" % "1.1.0" % "test",
  "ws.securesocial" %% "securesocial" % "master-SNAPSHOT",
  "commons-io" % "commons-io" % "2.4",
  "com.github.tototoshi" %% "slick-joda-mapper" % "1.2.0",
  "org.scalacheck" %% "scalacheck" % "1.11.1" % "test",
  "com.typesafe.akka" % "akka-testkit_2.11" % "2.3.9" % "test"
)

resolvers += Resolver.sonatypeRepo("snapshots") 

