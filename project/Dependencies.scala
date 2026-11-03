import sbt.*
import sbt.Keys.*

object Dependencies:

  object V:

    val circe      = "0.14.17"
    val laminar    = "18.0.0-M5"
    val catsEffect = "3.7.1"
    val scalajsDom = "2.8.1"
    val munit      = "1.3.6"

    val munitCatsEffect = "2.2.1"

  lazy val circe = libraryDependencies += "io.circe" %% "circe-core" % V.circe

  lazy val laminar = libraryDependencies += "com.raquo" %% "laminar" % V.laminar

  lazy val catsEffect =
    libraryDependencies += "org.typelevel" %% "cats-effect" % V.catsEffect

  lazy val scalajsDom =
    libraryDependencies += "org.scala-js" %% "scalajs-dom" % V.scalajsDom

  lazy val munit = libraryDependencies ++=
    Seq("org.scalameta" %% "munit" % V.munit % Test)

  lazy val munitCatsEffect = libraryDependencies +=
    "org.typelevel" %% "munit-cats-effect" % V.munitCatsEffect % Test
