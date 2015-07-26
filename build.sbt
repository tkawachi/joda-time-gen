organization := "com.github.tkawachi"

name := """joda-time-gen"""

scalaVersion := "2.11.7"

crossScalaVersions := Seq("2.10.5", "2.11.7")

libraryDependencies ++= Seq(
  "org.scalacheck" %% "scalacheck" % "1.12.4",
  "joda-time" % "joda-time" % "2.8.1",
  "org.joda" % "joda-convert" % "1.7"
)

licenses := Seq("MIT" -> url("http://opensource.org/licenses/MIT"))

scmInfo := Some(ScmInfo(
  url("https://github.com/tkawachi/joda-time-gen/"),
  "scm:git:github.com:tkawachi/joda-time-gen.git"
))

