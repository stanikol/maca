lazy val scalaV = "2.11.8"

lazy val server = (project in file("server")).settings(
  scalaVersion := scalaV,
  scalaJSProjects := Seq(client),
  pipelineStages in Assets := Seq(scalaJSPipeline),
  pipelineStages := Seq(digest, gzip),
  // triggers scalaJSPipeline when using compile or continuous compilation
  compile in Compile <<= (compile in Compile) dependsOn scalaJSPipeline,
  libraryDependencies ++= Seq(
    "com.h2database" % "h2" % "1.4.192",
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
    filters,
    cache,
    specs2 % Test
  ),
  // Compile the project before generating Eclipse files, so that generated .scala or .class files for views and routes are present
  EclipseKeys.preTasks := Seq(compile in Compile)
).enablePlugins(PlayScala).
  dependsOn(sharedJvm)

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

lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared")).
  settings(scalaVersion := scalaV).
  jsConfigure(_ enablePlugins ScalaJSWeb)

lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

// loads the server project at sbt startup
onLoad in Global := (Command.process("project server", _: State)) compose (onLoad in Global).value


fork in run := true