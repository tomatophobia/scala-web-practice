package com.easywritten.scrum.infrastructure.persistence.postgresql.item

import com.easywritten.scrum.domain.model.item.{Item, ItemRepository}
import doobie._
import doobie.implicits._
import zio.interop.catz._
import zio.{Has, Task, URLayer, ZLayer}

object PostgresItemRepository {
  val layer: URLayer[Has[Transactor[Task]], ItemRepository] =
    ZLayer.fromService { trx =>
      new ItemRepository.Service {
        override def getItems: Task[List[Item]] =
          Statements.getItems.to[List].transact(trx)
      }
    }

  object Statements {
    def getItems: Query0[Item] =
      sql"""
        |SELECT *
        |FROM items
        |""".stripMargin.query
  }
}
