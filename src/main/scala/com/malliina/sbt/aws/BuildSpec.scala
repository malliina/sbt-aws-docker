package com.malliina.sbt.aws

import java.nio.charset.StandardCharsets

import com.malliina.sbt.aws.Yaml._
import sbt.{File, IO}

import scala.concurrent.duration.{DurationInt, FiniteDuration}

case class KeyValue(key: String, value: String)

case class BuildSpecConf(envVars: Seq[KeyValue] = Nil,
                         install: Seq[String] = Nil,
                         preBuild: Seq[String] = Nil,
                         build: Seq[String] = Nil,
                         postBuild: Seq[String] = Nil,
                         artifacts: Seq[String] = Nil,
                         discardPaths: Option[Boolean] = None,
                         baseDirectory: Option[String] = None,
                         sections: Seq[Section] = Nil) {
  def withBeanstalk(conf: BeanstalkConf): BuildSpecConf =
    copy(sections = sections :+ BuildSpec.beanstalkExtension(conf))
}

case class BeanstalkConf(serviceRole: String,
                         image: String,
                         computeType: String = BeanstalkConf.SmallComputeType,
                         timeout: FiniteDuration = 60.minutes)

object BeanstalkConf {
  val SmallComputeType = "BUILD_GENERAL1_SMALL"
}

object BuildSpec {
  val FileName = "buildspec.yml"

  def write(conf: BuildSpecConf, file: File): String = writeYaml(forArtifacts(conf), file)

  def writeForArtifact(buildCommand: String, artifacts: Seq[String], yamlDest: File) = {
    val conf = BuildSpecConf(build = Seq(buildCommand), artifacts = artifacts)
    writeYaml(forArtifacts(conf), yamlDest)
  }

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

  def forArtifacts(conf: BuildSpecConf): YamlContainer = doc(
    Seq(
      Option(single("version", "0.1")),
      Option(row),
      toOpt(conf.envVars.nonEmpty)(
        section("environment_variables")(
          conf.envVars.map(kv => single(kv.key, s""""${kv.value}"""")): _*
        )
      ),
      toOpt(conf.envVars.nonEmpty)(row),
      Option(
        dynamicSection("phases")(
          commandSection("install", conf.install),
          commandSection("pre_build", conf.preBuild),
          commandSection("build", conf.build),
          commandSection("post_build", conf.postBuild)
        )
      ),
      toOpt(conf.artifacts.nonEmpty)(
        dynamicSection("artifacts")(
          Option(arr("files")(conf.artifacts.map(ArrEntry.apply): _*)),
          conf.baseDirectory.map(dir => single("base-directory", dir)),
          conf.discardPaths.map(b => single("discard-paths", if (b) "yes" else "no"))
        )
      )
    ).flatten ++ conf.sections: _*
  )

  def forBeanstalk(buildSpec: BuildSpecConf,
                   beanstalk: BeanstalkConf) =
    forArtifacts(buildSpec.withBeanstalk(beanstalk))

  def beanstalkExtension(conf: BeanstalkConf): Section =
    section("eb_codebuild_settings")(
      single("CodeBuildServiceRole", conf.serviceRole),
      single("ComputeType", conf.computeType),
      single("Image", conf.image),
      single("Timeout", conf.timeout.toMinutes.toString)
    )

  def dynamicSection(name: String)(sections: Option[YamlDoc]*) =
    section(name)(sections.flatten: _*)

  def commandSection(name: String, commands: Seq[String]): Option[Section] = {
    toOpt(commands.nonEmpty)(
      section(name)(
        arr("commands")(
          commands.map(ArrEntry.apply): _*
        )
      )
    )
  }

  def toOpt[T](p: Boolean)(v: T) = if (p) Option(v) else None
}
