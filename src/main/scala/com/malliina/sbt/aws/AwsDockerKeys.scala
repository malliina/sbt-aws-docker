package com.malliina.sbt.aws

import com.typesafe.sbt.packager.docker.CmdLike
import sbt._

object AwsDockerKeys extends AwsDockerKeys

trait AwsDockerKeys {
  val logger = taskKey[Logger]("Logger helper")

  // Docker keys
  val dockerFileInit = settingKey[Seq[CmdLike]]("The first lines in the Dockerfile.")
  val dockerBaseDir = settingKey[String]("WORKDIR")
  val dockerExecutable = settingKey[String]("Docker executable script")
  val dockerZip = taskKey[File]("Zips the app")
  val dockerZipTarget = taskKey[File]("The target zip file")
  val dockerArtifacts = taskKey[Seq[File]]("Build output artifacts for buildspec.yml")
  // CodeBuild keys
  val buildSpecFile = taskKey[File]("The buildspec.yml file for AWS CodeBuild")
  val tempSpecFile = settingKey[File]("Temporary buildspec-temp.yml")
  val createBuildSpec = taskKey[File]("Builds and returns the AWS CodeBuild buildspec.yaml file")
  val codeBuildArtifacts = taskKey[Seq[String]]("buildspec.yml entries under array files")
  val buildSpecConf = taskKey[BuildSpecConf]("Conf for buildspec.yml")
  val buildSpecBuildCommand = settingKey[String]("Default build command to run in CodeBuild, defaults to 'sbt docker:stage'")
  // Beanstalk keys
  val ebConfigFile = taskKey[File]("Beanstalk config file")
  val tempEbConfigFile = taskKey[File]("Temporary beanstalk config file")
  val ebLabel = taskKey[String]("Beanstalk app version")
  val ebDeploy = taskKey[Unit]("Deploys the app to Elastic Beanstalk")
  val localEbDeploy = taskKey[Unit]("Deploys a locally packaged .zip to Beanstalk")
}
