package com.easywritten.scrum

import cats.effect.Blocker
import com.easywritten.scrum.application.ItemApplicationService
import com.easywritten.scrum.config.AppConfig
import com.easywritten.scrum.infrastructure.persistence.postgresql.item.PostgresItemRepository
import doobie.hikari.HikariTransactor
import doobie.util.transactor.Transactor
import zio.blocking.{Blocking, blocking}
import zio.interop.catz._
import zio.{Has, Task, ULayer, ZEnv, ZIO, ZLayer}

object Wirings {

  // connect execution context, connectEC는 뭐지?
  // blocker 또는 blockingEC는 뭐지?
  // cats.effect.Blocker 는 뭘 제공하는 거지? => 우리 코드
  // ZIO.descriptor.map(_.executor.asEC) 이건 뭘 얻어내는 거지? => 블로그 코드
  // diamond-server에서 Transactor 얻어내는 방식과 블로그에서 Transactor 얻어내는 방식의 차이는 무엇?
  // 일단 ExecutionContext가 무엇인지부터...
  val transactorLayer: ZLayer[Has[AppConfig] with Blocking, Throwable, Has[Transactor[Task]]] =
    ZLayer.fromServiceManaged { config =>
      for {
        connectEC  <- ZIO.descriptor.map(_.executor.asEC).toManaged_
        blockingEC <- blocking { ZIO.descriptor.map(_.executor.asEC) }.toManaged_
        managed <-
          HikariTransactor
            .newHikariTransactor[Task](
              driverClassName = "org.postgresql.Driver",
              url = config.db.url,
              user = config.db.username,
              pass = config.db.password,
              connectEC = connectEC,
              blocker = Blocker.liftExecutionContext(blockingEC)
            )
            .toManagedZIO
      } yield managed
    }

  val postgresLayer = PostgresItemRepository.layer

  val applicationServiceLayer = ItemApplicationService.live

  val zenv = ZLayer.requires[ZEnv]

  def layers(configLayer: ULayer[Has[AppConfig]]): ZLayer[ZEnv, Throwable, ScrumEnv] =
    (zenv ++ ((zenv ++ configLayer >>> transactorLayer) >>> postgresLayer)) >>> applicationServiceLayer

}
