import com.typesafe.sbt.SbtGit._

name := """IT5041-web"""

javaOptions in Test += "-Dconfig.file=conf/test.conf"

versionWithGit

scalariformSettings

lazy val root = (project in file(".")).enablePlugins(PlayScala)

(testOptions in Test) += Tests.Argument(TestFrameworks.ScalaTest, "-u", "target/report")

ScoverageSbtPlugin.ScoverageKeys.coverageExcludedPackages := "<empty>;controllers.javascript;controllers.ref;securesocial.*;views.*"

scalaVersion := "2.11.1"

seq(bowerSettings : _*)

libraryDependencies ++= Seq(
  cache,
  jdbc,
  "com.typesafe.slick" %% "slick" % "2.1.0",
  "com.typesafe.play" %% "play-slick" % "0.8.0",
  "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
  "org.mindrot" % "jbcrypt" % "0.3m",
  "org.scalatestplus" %% "play" % "1.1.0" % "test",
  "ws.securesocial" %% "securesocial" % "master-SNAPSHOT",
  "com.github.tototoshi" %% "slick-joda-mapper" % "1.2.0",
  "org.webjars" % "bootstrap" % "3.3.2",
  "org.webjars" % "jquery" % "2.1.3",
  "org.webjars" % "angularjs" % "1.3.0",
  "org.webjars" % "angular-ui" % "0.4.0-3",
  "org.webjars" % "angular-ui-router" % "0.2.11-1",
  "org.webjars" % "font-awesome" % "4.2.0", 
  "org.webjars" % "lodash" % "2.4.1-6",
  "org.webjars" % "angular-ui-bootstrap" % "0.11.2",
  "org.webjars" % "select2" % "3.5.1",
  "org.webjars" % "angular-ui-select" % "0.8.3",
  "org.webjars" % "ui-grid" % "3.0.0-rc.11",
  "org.scalacheck" %% "scalacheck" % "1.11.1" % "test",
  "com.typesafe.akka" % "akka-testkit_2.11" % "2.3.9" % "test"
)

resolvers += Resolver.sonatypeRepo("snapshots") 

