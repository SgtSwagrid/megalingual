import IdeSettings.packagePrefix
import sbt._
import sbt.Keys._
import sbtunidoc.BaseUnidocPlugin.autoImport.*
import sbtunidoc.ScalaUnidocPlugin

val scala3 = "3.9.0"

ThisBuild / scalaVersion := scala3

ThisBuild / scalacOptions ++= Seq(
  "-explain",
  "-explain-types",
  "-explain-cyclic",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Wunused:all",
)

// Every project is named after the library so as not to clash with the
// projects of a build that includes this one by reference. Each module's
// sources are pinned to this build's base, which a matrix would otherwise
// resolve against the working directory, not this build's when another build
// includes it.

/** Languages, their names, flags and plural rules, with no Laminar. */
lazy val megalingual = projectMatrix
  .in(file("core"))
  .settings(
    name            := "megalingual",
    packagePrefix   := "com.alecdorrington.megalingual",
    sourceDirectory := (ThisBuild / baseDirectory).value / "core" / "src",
    Dependencies.circe,
    Dependencies.munit,
  )
  .jvmPlatform(
    scalaVersions = Seq(scala3),
    // An sbt 2.0.x classloader bug hides Test-scoped dependencies from
    // in-process test runs (NoClassDefFoundError: munit/FunSuite).
    settings = Seq(Test / fork := true),
  )
  .jsPlatform(scalaVersions = Seq(scala3))

/** The Laminar picker, and what else touches a browser's page. */
lazy val `megalingual-laminar` = projectMatrix
  .in(file("laminar"))
  .dependsOn(megalingual)
  .settings(
    name            := "megalingual-laminar",
    packagePrefix   := "com.alecdorrington.megalingual.laminar",
    sourceDirectory := (ThisBuild / baseDirectory).value / "laminar" / "src",
    Dependencies.laminar,
    Dependencies.munit,
  )
  .jsPlatform(scalaVersions = Seq(scala3))

lazy val `megalingual-root` = project
  .in(file("."))
  .enablePlugins(ScalaUnidocPlugin)
  .aggregate(megalingual.projectRefs ++ `megalingual-laminar`.projectRefs *)
  .settings(
    publish / skip := true,

    // Both modules documented from JavaScript, where both are built, so that
    // the core's pages link to the picker's.
    ScalaUnidoc / unidoc / unidocProjectFilter := inProjects(
      megalingual.js(scala3),
      `megalingual-laminar`.js(scala3),
    ),
    ScalaUnidoc / unidoc / scalacOptions ++= Seq("-project", "Megalingual"),
  )
