package com.easywritten.scrum.domain.model.item

final case class ItemId(value: Long) extends AnyVal

final case class Item(
    id: ItemId,
    title: String
)
