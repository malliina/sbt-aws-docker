package com.malliina.sbt.aws

import com.typesafe.sbt.packager.docker.{Cmd, CmdLike, DockerPlugin, ExecCmd}

object DockerFiles {
  def from(image: String) = Cmd("FROM", image)

  def workDir(dir: String) = Cmd("WORKDIR", dir)

  def makeExec(script: String) = execRun("chmod" :: "u+x" :: script :: Nil)

  def expose(ports: Seq[Int]) = Cmd("EXPOSE", ports.mkString(" "))

  def userCmd(username: String) = Cmd("USER", username)

  def entrypoint(entry: Seq[String]) = ExecCmd("ENTRYPOINT", entry: _*)

  def execCmd(cmd: Seq[String]) = ExecCmd("CMD", cmd: _*)

  def makeAdd(dockerBaseDirectory: String): CmdLike = {
    val files = dockerBaseDirectory.split(DockerPlugin.UnixSeparatorChar)(1)
    Cmd("ADD", s"$files /$files")
  }

  def makeChown(daemonUser: String, daemonGroup: String, directories: Seq[String]): CmdLike =
    execRun("chown" :: "-R" :: s"$daemonUser:$daemonGroup" :: directories.toList)

  def execRun(cmd: Seq[String]) = ExecCmd("RUN", cmd: _*)
}
