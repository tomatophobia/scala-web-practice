package com.easywritten.scrum

import cats.syntax.all._
import com.easywritten.scrum.config.AppConfig
import com.easywritten.scrum.endpoint.ItemEndpoints
import org.http4s._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.syntax.kleisli._
import pureconfig._
import pureconfig.generic.auto._
import sttp.tapir.server.http4s.ztapir.ZHttp4sServerInterpreter
import sttp.tapir.swagger.http4s.SwaggerHttp4s
import zio.clock.Clock
import zio.interop.catz._
import zio.{App, ExitCode, RIO, URIO, ZEnv, ZIO, ZLayer}

// TODO 각종 의존성 조립 좀 더 분리
object ScrumApp extends App {

  // Documentation
  val yaml: String = {
    import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
    import sttp.tapir.openapi.circe.yaml._
    OpenAPIDocsInterpreter
      .serverEndpointsToOpenAPI(ItemEndpoints.all, "scrum4s endpoints", "0.0.1")
      .toYaml
  }

  val serverRoutes: HttpRoutes[RIO[Clock with ScrumEnv, *]] =
    ZHttp4sServerInterpreter.from(ItemEndpoints.all).toRoutes

  // Starting the server
  val program: ZIO[ZEnv, Throwable, Unit] = {
    for {
      appConfig <- ZIO.effect(ConfigSource.default.loadOrThrow[AppConfig])

      configLayer = ZLayer.succeed(appConfig)

      serve <- ZIO
        .runtime[ZEnv with ScrumEnv]
        .flatMap { implicit runtime =>
          BlazeServerBuilder[RIO[ScrumEnv with Clock, *]](
            runtime.platform.executor.asEC
          )
            .bindHttp(appConfig.api.port, appConfig.api.endpoint)
            .withHttpApp(
              Router(
                "/" -> (serverRoutes <+> new SwaggerHttp4s(yaml).routes)
              ).orNotFound
            )
            .serve
            .compile
            .drain
        }
        .provideCustomLayer(Wirings.layers(configLayer))

    } yield serve
  }

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = program.exitCode

}
