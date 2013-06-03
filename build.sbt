organization := "me.lessis"

name := "hiroko"

version := "0.1.2-SNAPSHOT"

description := "An interface for the Heroku API"

scalacOptions += "-deprecation"

libraryDependencies ++= Seq(
  "net.databinder.dispatch" %% "dispatch-json4s-native" % "0.10.1",
  "org.slf4j" % "slf4j-jdk14" % "1.6.2")

seq(lsSettings: _*)

LsKeys.tags in LsKeys.lsync := Seq("dispatch", "heroku", "cloud")

seq(buildInfoSettings:_*)

sourceGenerators in Compile <+= buildInfo

buildInfoKeys := Seq[BuildInfoKey](version)

buildInfoPackage := "hiroko"

crossScalaVersions ++= Seq("2.9.3", "2.10.0", "2.10.1")

scalaVersion := "2.9.3"

publishTo := Some(Opts.resolver.sonatypeStaging)

publishMavenStyle := true

publishArtifact in Test := false

licenses <<= (version)(v =>
      Seq("MIT" ->
          url("https://github.com/softprops/hiroko/blob/%s/LICENSE" format v)))

homepage := some(url("https://github.com/softprops/hiroko/#readme"))

pomExtra := (
  <scm>
    <url>git@github.com:softprops/heroic.git</url>
    <connection>scm:git:git@github.com:softprops/hiroko.git</connection>
  </scm>
  <developers>
    <developer>
      <id>softprops</id>
      <name>Doug Tangren</name>
      <url>https://github.com/softprops</url>
    </developer>
  </developers>)
