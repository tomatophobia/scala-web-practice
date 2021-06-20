package com.easywritten.endpoint

import com.easywritten.application.ItemApplicationService
import io.circe.generic.auto._
import com.easywritten.domain.model.Item
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.{endpoint, stringBody}
import sttp.tapir.ztapir.{RichZEndpoint, ZServerEndpoint}

// TODO 에러 처리 status mapping
object ItemEndpoints {

  val getItems: ZServerEndpoint[Env, Unit, String, List[Item]] =
    endpoint.get
      .in("items")
      .errorOut(stringBody)
      .out(jsonBody[List[Item]])
      .summary("Item(Task 또는 User Story) 목록을 조회한다.")
      .zServerLogic(_ => ItemApplicationService.getItems)

  val all: List[ZServerEndpoint[Env, _, _, _]] =
    List(
      getItems
    )

  type Env = ItemApplicationService

}
