ThisBuild / credentials += Credentials(Path.userHome / ".sbt" / "sonatype_credentials")
ThisBuild / organization := "works.ideabank"
ThisBuild / organizationName := "ideabank"
ThisBuild / organizationHomepage := Some(url("https://www.ideabank.works"))

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/sean-ideabank/rudolf"),
    "scm:git@github.com:sean-ideabank/rudolf.git"
  )
)
ThisBuild / developers := List(
  Developer(
    id    = "sean",
    name  = "Sean Kim",
    email = "sean@ideabank.works",
    url   = url("http://www.ideabank.works")
  )
)

ThisBuild / description := "Minimal frameworks for web and apis"
ThisBuild / licenses := List("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt"))
ThisBuild / homepage := Some(url("https://github.com/sean-ideabank/rudolf"))

// Remove all additional repository other than Maven Central from POM
ThisBuild / pomIncludeRepository := { _ => false }
ThisBuild / publishTo := {
  val nexus = "https://s01.oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}
ThisBuild / sonatypeRepository := "https://s01.oss.sonatype.org/service/local"
ThisBuild / sonatypeCredentialHost := "s01.oss.sonatype.org"
ThisBuild / publishMavenStyle := true
