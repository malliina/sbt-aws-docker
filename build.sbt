import com.malliina.sbtutils.SbtProjects

lazy val awsDocker = SbtProjects.sbtPlugin("sbt-aws-docker")

scalaVersion := "2.12.6"
organization := "com.malliina"
libraryDependencies += "commons-io" % "commons-io" % "2.4"
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.5")
