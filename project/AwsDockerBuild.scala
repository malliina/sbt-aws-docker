import bintray.BintrayKeys.{bintray => bintrayConf, bintrayOrganization, bintrayRepository}
import sbt.Keys._
import sbt._

object AwsDockerBuild {
  lazy val awsDocker = Project("sbt-aws-docker", file("."))
    .enablePlugins(bintray.BintrayPlugin)
    .settings(projectSettings: _*)

  lazy val projectSettings = Seq(
    version := "0.0.1",
    scalaVersion := "2.10.6",
    organization := "com.malliina",
    sbtPlugin := true,
    libraryDependencies += "commons-io" % "commons-io" % "2.4",
    addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.1.4"),
    resolvers ++= Seq(
      Resolver.jcenterRepo,
      Resolver.bintrayRepo("malliina", "maven")
    ),
    bintrayOrganization in bintrayConf := None,
    bintrayRepository in bintrayConf := "sbt-plugins",
    publishMavenStyle := false,
    licenses +=("MIT", url("http://opensource.org/licenses/MIT"))
  )
}
