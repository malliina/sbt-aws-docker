import com.malliina.sbtutils.SbtProjects
import sbt.Keys._
import sbt._

object AwsDockerBuild {
  lazy val awsDocker = SbtProjects.sbtPlugin("sbt-aws-docker")
    .settings(projectSettings: _*)

  lazy val projectSettings = Seq(
    version := "0.0.1",
    organization := "com.malliina",
    libraryDependencies += "commons-io" % "commons-io" % "2.4",
    addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.1.4")
  )
}
