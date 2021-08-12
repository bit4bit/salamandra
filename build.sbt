val scala3Version = "3.0.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "scala3-simple",
    version := "0.1.0",

    scalaVersion := scala3Version,
    resolvers ++= Seq("public", "snapshots", "releases").map(Resolver.sonatypeRepo),
    Compile / scalacOptions += "-Xfatal-warnings",
    Test / logBuffered := false,
    libraryDependencies ++= Seq(
      "com.novocode" % "junit-interface" % "0.11" % "test",
      "org.scalactic" %% "scalactic" % "3.2.9",
      "org.scalatest" %% "scalatest" % "3.2.9" % "test",
      // Modules were resolved with conflicting cross-version suffixes
      // com.lihaoyi:sourcecode _3, _2.13
      // we download jar from maven and add to lib directory
      "com.lihaoyi" % "cask_3" % "0.7.11" % "provide",
      "com.lihaoyi" % "upickle_3" % "1.4.0",
      "org.postgresql" % "postgresql" % "42.2.8",
      "org.slf4j" % "slf4j-nop" % "1.6.4",
    ),
    ThisBuild / scalafixDependencies  += "org.scalalint" %% "rules" % "0.1.4"
  )
