resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "jgit-repo" at "http://download.eclipse.org/jgit/maven"

// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.3.4")

addSbtPlugin("com.typesafe.sbt" % "sbt-gzip" % "1.0.0")
     
addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.6.4")

addSbtPlugin("com.typesafe.sbt" % "sbt-scalariform" % "1.3.0")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.0.4")

addSbtPlugin("com.codacy" % "sbt-codacy-coverage" % "1.0.3")
