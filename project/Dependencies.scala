import sbt.*
import sbt.Keys.*

object Dependencies:

  object V:

    val circe   = "0.14.17"
    val laminar = "18.0.0-M5"
    val munit   = "1.3.6"

  lazy val circe = libraryDependencies += "io.circe" %% "circe-core" % V.circe

  lazy val laminar = libraryDependencies += "com.raquo" %% "laminar" % V.laminar

  lazy val munit = libraryDependencies ++=
    Seq("org.scalameta" %% "munit" % V.munit % Test)
