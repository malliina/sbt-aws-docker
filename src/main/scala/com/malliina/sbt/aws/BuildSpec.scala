package com.malliina.sbt.aws

import java.nio.charset.StandardCharsets

import com.malliina.sbt.aws.Yaml._
import sbt.{File, IO}

import scala.concurrent.duration.{DurationInt, FiniteDuration}

object BuildSpec {
  val FileName = "buildspec.yml"

  def writeForArtifact(sbtCommand: String, serviceRole: String, artifacts: Seq[String], dest: File) =
    writeYaml(forArtifacts(sbtCommand, serviceRole, artifacts), dest)

  /**
    * @param dest destination file
    * @param yaml contents to write
    * @return the written contents
    */
  def writeYaml(yaml: YamlContainer, dest: File): String = {
    val asString = stringifyDoc(yaml)
    IO.write(dest, asString, StandardCharsets.UTF_8)
    asString
  }

  def forArtifacts(sbtCommand: String, serviceRole: String, artifacts: Seq[String]): YamlContainer = doc(
    single("version", "0.1"),
    row,
    section("phases")(
      section("build")(
        arr("commands")(
          "echo Packaging started on `date` ...",
          s"sbt $sbtCommand",
          "echo Packaging completed on `date`"
        )
      )
    ),
    section("artifacts")(
      arr("files")(artifacts.map(ArrEntry.apply): _*)
    ),
    beanstalkExtension(serviceRole)
  )

  def beanstalkExtension(serviceRole: String,
                         computeType: String = "BUILD_GENERAL1_SMALL",
                         image: String = "hseeberger/scala-sbt",
                         timeout: FiniteDuration = 60.minutes) =
    section("eb_codebuild_settings")(
      single("CodeBuildServiceRole", serviceRole),
      single("ComputeType", computeType),
      single("Image", image),
      single("Timeout", timeout.toMinutes.toString)
    )
}
