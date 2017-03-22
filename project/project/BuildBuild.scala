import sbt.Keys._
import sbt._

object BuildBuild {

  lazy val settings = sbtPlugins ++ Seq(
    scalaVersion := "2.10.6",
    resolvers += ivyResolver("malliina bintray sbt", url("https://dl.bintray.com/malliina/sbt-plugins/"))
  )

  def ivyResolver(name: String, repoUrl: sbt.URL) =
    Resolver.url(name, repoUrl)(Resolver.ivyStylePatterns)

  def sbtPlugins = Seq(
    "com.malliina" %% "sbt-utils" % "0.6.1"
  ) map addSbtPlugin
}
