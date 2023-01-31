ThisBuild / scalaVersion := "3.2.1"
ThisBuild / organization := "com.ekinox"

lazy val dvdShop = (project in file("."))
  .settings(
    name := "DVD Shop",
    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.15",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % "test",
    libraryDependencies += "org.scalatest" %% "scalatest-flatspec" % "3.2.15" % "test",
    libraryDependencies += "org.typelevel" %% "cats-core" % "2.9.0",
    libraryDependencies += "org.typelevel" %% "cats-effect" % "3.4.5"
  )
