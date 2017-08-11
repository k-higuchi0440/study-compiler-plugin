lazy val commonSettings = Seq(
  version := "1.0",
  scalaVersion := "2.12.3",
  scalaSource in Compile := baseDirectory.value / "main" / "scala"
)

lazy val plugin = Project("compiler-plugin", file(".") / "src" / "plugin")
  .settings(
    commonSettings,
    libraryDependencies += "org.scala-lang" % "scala-compiler" % "2.12.3",
    mappings in (Compile, packageBin) +=
      (baseDirectory.value / "main" / "resources" / "scalac-plugin.xml") -> "scalac-plugin.xml"
  )

lazy val pluginDir = file(".") / "src" / "plugin" / "target" / "scala-2.12"
lazy val pluginJarName = "compiler-plugin_2.12-1.0.jar"

lazy val pluginClient = Project("compiler-plugin-client", file(".") / "src" / "client")
  .settings(
    commonSettings,
    scalacOptions ++= Seq(
//      "-Xshow-phases",
//      "-Xplugin-list",
//      "-Xprint:divbyzerocheck",
//      "-Ybrowse:divbyzerocheck",
      "-Xplugin:" + Path.absolute(pluginDir) / pluginJarName
    )
  )

