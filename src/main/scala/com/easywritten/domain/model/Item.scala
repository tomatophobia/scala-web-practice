package com.easywritten.domain.model

sealed trait Item

object Item {
  final case class Task() extends Item
  final case class UserStory() extends Item
}
