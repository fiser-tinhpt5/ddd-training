import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import sbt.Keys._

lazy val scalikejdbcVersion = "2.4.2"

lazy val spec2Version = "3.8.5"

lazy val mysqlConnectorVersion = "5.1.38"

lazy val flyWayVersion = "3.0.1"

lazy val skinnyORMVersion = "2.3.0"

lazy val commonSettings = Seq(
  name := "ddd_training",
  version := "1.0",
  scalaVersion := "2.11.7",
  libraryDependencies ++= Seq(
    "org.specs2" %% "specs2-core" % spec2Version % "test"
  )
)

lazy val `ddd_training` = (project in file("."))
  .enablePlugins(PlayScala)
  .dependsOn(application, domain, infrastructure)
  .aggregate(application, domain, infrastructure)
  .settings(commonSettings: _*)
  .settings(run := {
    (run in application in Compile).evaluated
  })

lazy val application = (project in file("application"))
  .enablePlugins(PlayScala)
  .enablePlugins(SbtWeb)
  .dependsOn(domain)
  .settings(commonSettings: _*)
  .settings(
    name += ".application",
    libraryDependencies ++= Seq(
      jdbc,
      cache,
      ws,
      "org.flywaydb" %% "flyway-play" % flyWayVersion
    ),
    routesGenerator := InjectedRoutesGenerator
  )

lazy val domain = (project in file("domain"))
  .dependsOn(infrastructure)
  .enablePlugins(PlayScala)
  .settings(commonSettings: _*)
  .settings(
    name += ".domain",
    scalaSource in Compile := baseDirectory.value / "src" / "main" / "scala",
    scalaSource in Test := baseDirectory.value / "src" / "test" / "scala",
    resourceDirectory in Compile := baseDirectory.value / "src" / "main" / "resoucre",
    resourceDirectory in Test := baseDirectory.value / "src" / "test" / "resoucre"
  )

lazy val infrastructure = (project in file("infrastructure"))
  .enablePlugins(PlayScala)
  .settings(commonSettings: _*)
  .settings(
    name += ".infrastructure",
    libraryDependencies ++= Seq(
      "org.scalikejdbc" %% "scalikejdbc"       % scalikejdbcVersion,
      "org.scalikejdbc" %% "scalikejdbc-test"   % scalikejdbcVersion   % "test",
      "mysql" % "mysql-connector-java" % mysqlConnectorVersion,
      "org.skinny-framework" %% "skinny-orm"      % skinnyORMVersion
    ),
    scalaSource in Compile := baseDirectory.value / "src" / "main" / "scala",
    scalaSource in Test := baseDirectory.value / "src" / "test" / "scala",
    resourceDirectory in Compile := baseDirectory.value / "src" / "main" / "resoucre",
    resourceDirectory in Test := baseDirectory.value / "src" / "test" / "resoucre"
  )


unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

SbtScalariform.scalariformSettings

ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(DoubleIndentClassDeclaration, true)
  .setPreference(PreserveDanglingCloseParenthesis, true)