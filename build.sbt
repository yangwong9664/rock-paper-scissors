
name := "yang-frontend-template"

scalaVersion := "2.12.12"

scalaSource in IntegrationTest := baseDirectory.value / "it"

parallelExecution in IntegrationTest := false
parallelExecution in Test := false

fork in Test := true
fork in IntegrationTest := true

libraryDependencies ++= {
  Seq(
    ws,
    guice,
    "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % "test, it",
    "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0" % Test,
    "de.leanovate.play-mockws" %% "play-mockws" % "2.8.0" % Test,
    "org.jsoup" % "jsoup" % "1.13.1" % Test,
    "com.github.tomakehurst" % "wiremock-standalone" % "2.27.2" % IntegrationTest
  )
}

dependencyUpdatesFailBuild := true
dependencyUpdatesFilter -= moduleFilter(name = "twirl-api")

scalastyleFailOnError := true

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xfatal-warnings")

coverageEnabled in (Test, compile) := true
coverageEnabled in (IntegrationTest, compile) := true
coverageEnabled in (Compile, compile) := false
coverageMinimum := 1
coverageFailOnMinimum := true
coverageHighlighting := true
coverageExcludedPackages := "<empty>;Reverse.*;view.*;.*(BuildInfo|Routes).*;"

lazy val root = (project in file(".")).enablePlugins(PlayScala).configs(IntegrationTest).settings(Defaults.itSettings: _*)

assemblyJarName in assembly := "yang-frontend-template.jar"
