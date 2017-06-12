import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.agecomp",
      scalaVersion := "2.12.1",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "Hello",
    fork in run := true,
    libraryDependencies ++= Seq(
      scalaTest % Test,
      "org.scala-lang" % "scala-reflect" % "2.12.1",
      "com.typesafe.akka" %% "akka-actor" % "2.5.1",
      "org.scalafx" %% "scalafx" % "8.0.102-R11"
    ),
    scalacOptions := Seq("-unchecked", "-feature")
  )
