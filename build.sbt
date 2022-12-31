ThisBuild / scalaVersion     := "2.13.10"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.github.windbird123.caskweb"
ThisBuild / organizationName := "windbird123"

val flywayVersion        = "8.5.12" // manages database migrations
val laminarVersion       = "0.14.2" // functional reactive programming (FRP) library
val waypointVersion      = "0.5.0"  // router for Laminar for URL matching and managing URL transitions
val postgresVersion      = "42.3.6" // Java database connectivity (JDBC) driver for PostgreSQL
val scalaJavaTimeVersion = "2.4.0"  // an implementation of the java.time package for Scala
val caskVersion          = "0.8.3"
val slf4jVersion         = "2.0.6"  // logging framework
val logbackVersion       = "1.4.5"

Global / onChangedBuildSource := ReloadOnSourceChanges

val sharedSettings = Seq(
  libraryDependencies ++= Seq(
  ),
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding",
    "utf8",
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-unchecked",
    "-Xfatal-warnings",
    "-Ymacro-annotations"
  )
)

val publicDev  = taskKey[String]("output directory for `npm run dev`")
val publicProd = taskKey[String]("output directory for `npm run build`")

lazy val root = (project in file("."))
  .aggregate(backend, frontend, shared)
  .settings(name := "cask-web")

lazy val backend = (project in file("backend"))
  .settings(
    name := "backend",
    libraryDependencies ++= Seq(
      "com.lihaoyi" %% "cask" % caskVersion
//      "org.jboss.xnio" % "xnio-nio"        % "3.8.8.Final",
//      "org.slf4j"      % "slf4j-api"       % slf4jVersion,
//      "ch.qos.logback" % "logback-classic" % logbackVersion,
//      "org.postgresql" % "postgresql"      % postgresVersion,
//      "org.flywaydb"   % "flyway-core"     % flywayVersion
    ),
    Test / fork := true,
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )
  .enablePlugins(JavaAppPackaging)
  .settings(sharedSettings)
  .enablePlugins(FlywayPlugin)
  .settings(
    flywayUrl                  := "jdbc:postgresql://localhost:5432/postgres",
    flywayUser                 := "postgres",
    flywayPassword             := "",
    assembly / assemblyJarName := "app.jar",
    assembly / assemblyMergeStrategy := {
      case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
      case x                                   => MergeStrategy.last
    }
  )
  .dependsOn(shared)

lazy val frontend = (project in file("frontend"))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name                            := "frontend",
    scalaJSUseMainModuleInitializer := true,
    scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.ESModule) },
    scalaJSLinkerConfig ~= { _.withSourceMap(false) },
    libraryDependencies ++= Seq(
      "com.raquo"         %%% "laminar"         % laminarVersion,
      "com.raquo"         %%% "waypoint"        % waypointVersion,
      "io.github.cquiroz" %%% "scala-java-time" % scalaJavaTimeVersion
    ),
    publicDev  := linkerOutputDirectory((Compile / fastLinkJS).value).getAbsolutePath,
    publicProd := linkerOutputDirectory((Compile / fullLinkJS).value).getAbsolutePath
  )
  .settings(sharedSettings)
  .dependsOn(shared)

lazy val shared = (project in file("shared"))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    scalaJSLinkerConfig ~= { _.withSourceMap(false) },
    scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.ESModule) }
  )
  .settings(sharedSettings)

def linkerOutputDirectory(v: Attributed[org.scalajs.linker.interface.Report]): File =
  v.get(scalaJSLinkerOutputDirectory.key).getOrElse {
    throw new MessageOnlyException(
      "Linking report was not attributed with output directory. " +
        "Please report this as a Scala.js bug."
    )
  }
