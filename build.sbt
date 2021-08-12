val scala3Version = "3.0.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "scala3-simple",
    version := "0.1.0",

    scalaVersion := scala3Version,
    Compile / scalacOptions += "-Xfatal-warnings",
    Test / logBuffered := false,
    libraryDependencies ++= Seq(
      "com.novocode" % "junit-interface" % "0.11" % "test",
      "org.scalactic" %% "scalactic" % "3.2.9",
      "org.scalatest" %% "scalatest" % "3.2.9" % "test"),
    ThisBuild / scalafixDependencies  += "org.scalalint" %% "rules" % "0.1.4"
  )
