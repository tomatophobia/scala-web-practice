package com.easywritten.scrum.application

import com.easywritten.scrum.domain.model.item.{Item, ItemRepository}
import zio.console.Console
import zio.{Task, ZIO, ZLayer}

// TODO 적절한 에러 타입
object ItemApplicationService {
  trait Service {
    def getItems: Task[List[Item]]
  }

  val live: ZLayer[Console with ItemRepository, Throwable, ItemApplicationService] =
    ZLayer.fromServices[
      Console.Service,
      ItemRepository.Service,
      ItemApplicationService.Service
    ] { (console, itemRepository) =>
      new Service {
        override def getItems: Task[List[Item]] =
          console.putStr("get Items Success") *> itemRepository.getItems

      }
    }

  def getItems: ZIO[ItemApplicationService, Throwable, List[Item]] =
    ZIO.accessM(_.get.getItems)
}
