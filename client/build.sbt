name := """message client"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.8"

// https://mvnrepository.com/artifact/dom4j/dom4j
libraryDependencies += "dom4j" % "dom4j" % "1.6"

libraryDependencies += "org.mockito" % "mockito-core" % "2.1.0"

libraryDependencies += "com.google.protobuf" % "protobuf-java" % "3.1.0"


libraryDependencies += javaWs
