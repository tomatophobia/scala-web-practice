package com.easywritten.scrum.domain.model.item

import zio.{Task, ZIO}

object ItemRepository {

  trait Service {
    def getItems: Task[List[Item]]
  }

  def getItems: ZIO[ItemRepository, Throwable, List[Item]] =
    ZIO.accessM(_.get.getItems)

}
