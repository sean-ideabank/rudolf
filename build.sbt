val scala3Version = "3.0.2"

ThisBuild / name := "rudolf"
ThisBuild / version := "0.1.1"
ThisBuild / versionScheme := Some("semver-spec")
ThisBuild / scalaVersion := scala3Version

sonatypeRepository := "https://s01.oss.sonatype.org/service/local"
sonatypeCredentialHost := "s01.oss.sonatype.org"

ThisBuild / libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-simple" % "2.0.0-alpha5",
  "org.eclipse.jetty" % "jetty-server" % "11.0.6",
  "org.apache.commons" % "commons-configuration2" % "2.7",
  "org.apache.velocity" % "velocity-engine-core" % "2.3",
  "commons-beanutils" % "commons-beanutils" % "1.9.4",
  "com.novocode" % "junit-interface" % "0.11" % "test"
  )
