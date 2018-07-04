scalaVersion := "2.12.6"

resolvers += ivyResolver("malliina bintray sbt", url("https://dl.bintray.com/malliina/sbt-plugins/"))

def ivyResolver(name: String, repoUrl: sbt.URL) =
  Resolver.url(name, repoUrl)(Resolver.ivyStylePatterns)

Seq(
  "com.malliina" %% "sbt-utils" % "0.8.0",
  "org.foundweekends" % "sbt-bintray" % "0.5.4"
) map addSbtPlugin
