ThisBuild / description :=
  "Languages with their names and flags, and a Laminar picker for choosing one."

ThisBuild / homepage := Some(uri("https://alecdorrington.com/megalingual"))

ThisBuild / organization         := "com.alecdorrington"
ThisBuild / organizationName     := "Alec Dorrington"
ThisBuild / organizationHomepage := Some(uri("https://alecdorrington.com"))

ThisBuild / versionScheme := Some("strict")

ThisBuild / licenses :=
  List("MIT" -> uri("https://opensource.org/licenses/MIT"))

ThisBuild / developers := List(Developer(
  id = "SgtSwagrid",
  name = "Alec Dorrington",
  email = "alecdorrington@gmail.com",
  url = uri("https://github.com/SgtSwagrid"),
))
