import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.agecomp",
      scalaVersion := "2.12.1",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "Hello",
    libraryDependencies ++= Seq(
      scalaTest % Test,
      "org.scala-lang" % "scala-reflect" % "2.12.1",
      "com.typesafe.akka" %% "akka-actor" % "2.5.1"
    )
  )
