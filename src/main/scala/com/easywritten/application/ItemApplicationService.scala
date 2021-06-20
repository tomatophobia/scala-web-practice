package com.easywritten.application

import com.easywritten.domain.model.Item
import zio.console.Console
import zio.{ZIO, ZLayer}

// TODO 적절한 에러 타입
object ItemApplicationService {
  trait Service {
    def getItems: ZIO[Any, String, List[Item]]
  }

  val live: ZLayer[Console, String, ItemApplicationService] =
    ZLayer.fromService[Console.Service, ItemApplicationService.Service] { console =>
      new Service {
        override def getItems: ZIO[Any, String, List[Item]] =
          console.putStr("get Items Success")
            .mapError(_.getMessage) *> {
            ZIO.succeed(List(Item.Task(), Item.UserStory()))
          }
      }
    }

  def getItems: ZIO[ItemApplicationService, String, List[Item]] = ZIO.accessM(_.get.getItems)
}
