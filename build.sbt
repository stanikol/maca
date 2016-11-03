lazy val scalaV = "2.11.8"

lazy val server = (project in file("server")).settings(
  scalaVersion := scalaV,
  scalaJSProjects := Seq(client, admin),
  pipelineStages in Assets := Seq(scalaJSPipeline),
  pipelineStages := Seq(digest, gzip),
  // triggers scalaJSPipeline when using compile or continuous compilation
  compile in Compile <<= (compile in Compile) dependsOn scalaJSPipeline,
  libraryDependencies ++= Seq(
//    "com.h2database" % "h2" % "1.4.192",
    "com.typesafe.play" % "play-slick_2.11" % "2.0.2",
    "com.typesafe.play" %% "play-slick-evolutions" % "2.0.2",
    "com.vmunier" %% "scalajs-scripts" % "1.0.0",
    "org.postgresql" % "postgresql" % "9.4.1211",
    "com.github.tminglei" %% "slick-pg" % "0.14.3",
    "com.github.tminglei" %% "slick-pg_play-json" % "0.14.3",
    "com.sksamuel.scrimage" %% "scrimage-core" % "2.1.0",
    "org.webjars" %% "webjars-play" % "2.5.0",
    "org.webjars" % "bootstrap" % "3.3.7",
//    "org.webjars" % "jquery" % "3.1.1",
    "org.webjars" % "jquery" % "2.1.3",
    "org.webjars" % "materializecss" % "0.97.7",
    "com.lihaoyi" %% "upickle" % "0.4.3",
    "org.webjars" % "font-awesome" % "4.6.3",
    // silhouette
    "org.webjars" % "requirejs" % "2.3.1",
    "com.adrianhurt" %% "play-bootstrap" % "1.1-P25-B3",	// Add bootstrap helpers and field constructors (http://adrianhurt.github.io/play-bootstrap/)
    "com.mohiva" %% "play-silhouette" % "4.0.0",
    "com.mohiva" %% "play-silhouette-password-bcrypt" % "4.0.0",
    "com.mohiva" %% "play-silhouette-persistence" % "4.0.0",
    "com.mohiva" %% "play-silhouette-crypto-jca" % "4.0.0",
    "com.mohiva" %% "play-silhouette-testkit" % "4.0.0" % "test",
    "net.codingwell" %% "scala-guice" % "4.0.1",
//    "com.iheart" %% "ficus" % "1.2.6",
    "com.iheart" %% "ficus" % "1.3.0",
    "com.typesafe.play" %% "play-mailer" % "5.0.0",
    //
    filters,
    cache,
    specs2 % Test
  ),
  // Compile the project before generating Eclipse files, so that generated .scala or .class files for views and routes are present
  EclipseKeys.preTasks := Seq(compile in Compile),
  resolvers += "Atlassian Releases" at "https://maven.atlassian.com/public/" ,
  resolvers += Resolver.jcenterRepo,
  scalacOptions ++= Seq("-deprecation")
).enablePlugins(PlayScala, JavaAppPackaging).dependsOn(sharedJvm)

lazy val client = (project in file("client")).settings(
  scalaVersion := scalaV,
  persistLauncher := true,
  scalacOptions ++= Seq("-Xmax-classfile-name","78"),
  persistLauncher in Test := false,
  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.9.1",
    "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.6",
    "com.thoughtworks.binding" %%% "dom" % "10.0.0-M1",
    "com.thoughtworks.binding" %%% "futurebinding" % "10.0.0-M1",
    "fr.hmil" %%% "roshttp" % "1.1.0",
    "be.doeraene" %%% "scalajs-jquery" % "0.9.0",
    "io.udash" %%% "udash-jquery" % "1.0.0",
    "com.lihaoyi" %%% "upickle" % "0.4.3"
  )
).enablePlugins(ScalaJSPlugin, ScalaJSWeb).
  dependsOn(sharedJs)

lazy val admin = (project in file("admin")).settings(
  scalaVersion := scalaV,
  persistLauncher := true,
  scalacOptions ++= Seq("-Xmax-classfile-name","78"),
  persistLauncher in Test := false,
  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.9.1",
    "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.6",
    "com.thoughtworks.binding" %%% "dom" % "10.0.0-M1",
    "com.thoughtworks.binding" %%% "futurebinding" % "10.0.0-M1",
    "fr.hmil" %%% "roshttp" % "1.1.0",
    "be.doeraene" %%% "scalajs-jquery" % "0.9.0",
    "io.udash" %%% "udash-jquery" % "1.0.0",
    "com.lihaoyi" %%% "upickle" % "0.4.3"
  )
).enablePlugins(ScalaJSPlugin, ScalaJSWeb).
  dependsOn(sharedJs)

lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared")).
  settings(scalaVersion := scalaV,
    libraryDependencies += "com.lihaoyi" %% "upickle" % "0.4.3"
  ).
  jsConfigure(_ enablePlugins ScalaJSWeb)

lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

// loads the server project at sbt startup
onLoad in Global := (Command.process("project server", _: State)) compose (onLoad in Global).value

herokuAppName in Compile := "maca"
mainClass in assembly := Some("play.core.server.NettyServer")
assemblyMergeStrategy in assembly := {
  case PathList("javax", "servlet", xs @ _*)         => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
  case "application.conf"                            => MergeStrategy.concat
  case "unwanted.txt"                                => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}
enablePlugins(JavaAppPackaging)

fork in run := true