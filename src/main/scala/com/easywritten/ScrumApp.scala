package com.easywritten

import cats.syntax.all._
import com.easywritten.application.ItemApplicationService
import com.easywritten.endpoint.ItemEndpoints
import io.circe.generic.auto._
import org.http4s._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.syntax.kleisli._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._
import sttp.tapir.server.http4s.ztapir.ZHttp4sServerInterpreter
import sttp.tapir.swagger.http4s.SwaggerHttp4s
import sttp.tapir.ztapir._
import zio.clock.Clock
import zio.console.Console
import zio.interop.catz._
import zio.{App, ExitCode, Has, IO, RIO, UIO, URIO, ZEnv, ZIO, ZLayer}

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

  val serverRoutes: HttpRoutes[RIO[ItemApplicationService with Clock, *]] =
    ZHttp4sServerInterpreter.from(ItemEndpoints.all).toRoutes

  // Starting the server
  val serve: ZIO[ZEnv with ItemApplicationService, Throwable, Unit] =
    ZIO.runtime[ZEnv with ItemApplicationService].flatMap { implicit runtime =>
      BlazeServerBuilder[RIO[ItemApplicationService with Clock, *]](
        runtime.platform.executor.asEC
      )
        .bindHttp(8080, "localhost")
        .withHttpApp(
          Router(
            "/" -> (serverRoutes <+> new SwaggerHttp4s(yaml).routes)
          ).orNotFound
        )
        .serve
        .compile
        .drain
    }

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    serve.provideCustomLayer(ItemApplicationService.live).exitCode

}
