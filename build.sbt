val scala3Version = "2.13.6"

lazy val root = project
  .in(file("."))
  .settings(
    name := "scalamandra",
    version := "0.1.0",

    scalaVersion := scala3Version,
    Compile / scalacOptions += "-Xfatal-warnings",
    Compile / scalacOptions += "-Ytasty-reader",
    Compile / scalacOptions += "-deprecation",
    Test / logBuffered := false,
    libraryDependencies ++= Seq(
      "com.novocode" % "junit-interface" % "0.11" % "test",
      "org.scalactic" %% "scalactic" % "3.2.9",
      "org.scalatest" %% "scalatest" % "3.2.9" % "test",
      "com.lihaoyi" %% "cask" % "0.7.11",
      "com.lihaoyi" %% "upickle" % "1.4.0",
      "com.lihaoyi" %% "requests" % "0.6.5",
      "org.slf4j" % "slf4j-simple" % "1.6.4",
      "org.scalikejdbc" %% "scalikejdbc"       % "3.5.0",
      "com.h2database"  %  "h2"                % "1.4.200",
      "org.postgresql" % "postgresql" % "42.2.8",
    ),
    ThisBuild / scalafixDependencies  += "org.scalalint" %% "rules" % "0.1.4"
  )
