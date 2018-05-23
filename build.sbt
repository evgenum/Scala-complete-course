import _root_.sbt.Keys._

lazy val generalSettings =
  Seq(
    name := "scala-course",
    version := "1.0",
    scalaVersion := "2.12.2",
    scalacOptions := Seq("-encoding",
                         "utf8",
                         "-feature",
                         "-unchecked",
                         "-deprecation",
                         "-target:jvm-1.8",
                         "-Ymacro-debug-lite",
                         "-language:experimental.macros"),
    resolvers ++= Seq(
      Resolver.jcenterRepo,
      Resolver.url("jCenter", url("http://jcenter.bintray.com/")),
      Resolver.sonatypeRepo("releases"),
      Resolver.sonatypeRepo("snapshots")),
    libraryDependencies ++= Seq(
      "org.scala-lang.modules" %% "scala-async" % "0.9.7",
      "org.scala-lang" % "scala-library" % "2.12.2",
      "org.scala-lang" % "scala-reflect" % "2.12.2",
      "org.typelevel" %% "cats-core" % "1.0.0-MF",
      "org.typelevel" %% "cats-free" % "1.0.0-MF",
      "org.typelevel" %% "cats-laws" % "1.0.0-MF",
      "org.xerial" % "sqlite-jdbc" % "3.7.2",
      "org.scalatest" %% "scalatest" % "3.0.1" % "test",
      "de.sciss" %% "coroutines-common" % "0.1.0",
      "org.mockito" % "mockito-core" % "1.9.5" % "test",
      "org.scalacheck" %% "scalacheck" % "1.13.4" % "test",
      "com.typesafe.akka" %% "akka-actor" % "2.5.3",
      "com.typesafe.akka" %% "akka-testkit" % "2.5.3" % Test,
      "com.typesafe.akka" %% "akka-remote" % "2.5.3",
      "com.typesafe.akka" %% "akka-slf4j" % "2.5.3",
      "com.softwaremill.macwire" %% "macros" % "2.3.0" % "provided",
      "com.softwaremill.macwire" %% "macrosakka" % "2.3.0" % "provided",
      "com.softwaremill.macwire" %% "util" % "2.3.0",
      "com.softwaremill.macwire" %% "proxy" % "2.3.0",
      "com.storm-enroute" %% "scalameter" % "0.9",
    )
  )
addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.4")

parallelExecution in ThisBuild := false

lazy val oop = config("oop") extend Test

lazy val assemblySettings = Seq(
  assemblyOutputPath in assembly := file(
    s"target/scala-course-assembly-${version.value}.jar"),
  mainClass in assembly := Some("lectures.oop.TreeTest"),
  test in assembly := {}
)

lazy val root = (project in file("."))
  .configs(oop)
  .settings(
    generalSettings,
    inConfig(oop)(Defaults.testTasks),
    testOptions in oop := Seq(Tests.Filter(_ startsWith "lectures.oop")),
    assemblySettings
  )
