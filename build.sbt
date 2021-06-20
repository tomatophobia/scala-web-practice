name := "scrum4s"

version := "0.0.1"

scalaVersion := "2.13.6"

libraryDependencies ++= Seq(
  "com.softwaremill.sttp.tapir" %% "tapir-core" % "0.17.19",
  "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % "0.17.19",
  "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % "0.17.19",
  "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % "0.17.19",
  "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % "0.17.19",
  "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-http4s" % "0.17.19",
  "com.softwaremill.sttp.tapir" %% "tapir-zio-http4s-server" % "0.17.19"
)

libraryDependencies ++= Seq("com.softwaremill.sttp.client3" %% "core" % "3.3.7")

libraryDependencies ++= Seq(
  "dev.zio" %% "zio" % "1.0.9",
  "dev.zio" %% "zio-streams" % "1.0.9",
  "dev.zio" %% "zio-interop-cats" % "2.5.1.0",
  "dev.zio" %% "zio-logging" % "0.5.11",
  "dev.zio" %% "zio-logging-slf4j" % "0.5.11"
)

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.6.1",
  "org.typelevel" %% "cats-effect" % "2.5.1"
)

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core" % "0.14.1",
  "io.circe" %% "circe-generic" % "0.14.1"
)

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-core" % "0.21.24",
  "org.http4s" %% "http4s-dsl" % "0.21.24",
  "org.http4s" %% "http4s-blaze-server" % "0.21.24",
  "org.http4s" %% "http4s-blaze-client" % "0.21.24"
)

// tapir examples가 이 로깅을 사용함
libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "ch.qos.logback" % "logback-core" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4"
)

addCompilerPlugin(
  "org.typelevel" % "kind-projector" % "0.13.0" cross CrossVersion.full
)
