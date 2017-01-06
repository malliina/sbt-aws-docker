package com.malliina.sbt.aws

import sbt._

object AwsDockerKeys extends AwsDockerKeys

trait AwsDockerKeys {
  val logger = taskKey[Logger]("Logger helper")

  // Docker keys
  val dockerBaseDir = settingKey[String]("WORKDIR")
  val dockerExecutable = settingKey[String]("Docker executable script")
  val dockerZip = taskKey[File]("Zips the app")
  val dockerZipTarget = taskKey[File]("The target zip file")
  val dockerArtifacts = taskKey[Seq[File]]("Build output artifacts for buildspec.yml")
  // CodeBuild keys
  val buildSpecFile = taskKey[File]("The buildspec.yml file for AWS CodeBuild")
  val tempSpecFile = settingKey[File]("Temporary buildspec-temp.yml")
  val createBuildSpec = taskKey[File]("Builds and returns the AWS CodeBuild buildspec.yaml file")
  val codeBuild = taskKey[Unit]("Prepare for CodePipeline deployment")
  val codeBuildServiceRole = settingKey[Option[String]]("CodeBuild service role")
  val codeBuildArtifacts = taskKey[Seq[String]]("buildspec.yml entries under array files")
  // Beanstalk keys
  val ebConfigFile = taskKey[File]("Beanstalk config file")
  val tempEbConfigFile = taskKey[File]("Temporary beanstalk config file")
  val ebLabel = taskKey[String]("Beanstalk app version")
  val ebDeploy = taskKey[Unit]("Deploys the app to Elastic Beanstalk")
  val localEbDeploy = taskKey[Unit]("Deploys a locally packaged .zip to Beanstalk")
}
