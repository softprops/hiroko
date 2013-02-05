organization := "me.lessis"

name := "hiroko"

version := "0.1.0"

description := "An interface for the Heroku API"

scalacOptions += "-deprecation"

libraryDependencies ++= Seq(
  "net.databinder.dispatch" %% "json4s-native" % "0.9.5",
  "org.slf4j" % "slf4j-jdk14" % "1.6.2")

seq(lsSettings: _*)

LsKeys.tags in LsKeys.lsync := Seq("dispatch", "heroku", "cloud")

seq(buildInfoSettings:_*)

sourceGenerators in Compile <+= buildInfo

buildInfoKeys := Seq[BuildInfoKey](version)

buildInfoPackage := "hiroko"

crossScalaVersions ++= Seq(
  "2.8.1", "2.8.2", "2.9.0-1",
  "2.9.1", "2.9.1-1", "2.9.2",
  "2.10.0")

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
