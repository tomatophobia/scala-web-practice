package com.easywritten.scrum.application

import enumeratum._

import scala.collection.immutable

// 구삼이 환경 변수 설정을 좀 더 엘레강스하게 할 방법이 있다고 했는데 기억 안남. 두물머리 기록 참고해야 할 듯
sealed trait AppEnv extends EnumEntry with Product with Serializable {
  def isDevelop: Boolean
  def isStaging: Boolean
  def isProd: Boolean
}

object AppEnv extends Enum[AppEnv] {
  val EnvVar: String = "SCRUM4S_ENV"

  def get: AppEnv = sys.env.get(EnvVar).map(withNameInsensitive).getOrElse(Development)

  final case object Development extends AppEnv {
    override def isDevelop: Boolean = true

    override def isStaging: Boolean = !isDevelop

    override def isProd: Boolean = !isDevelop
  }

  final case object Staging extends AppEnv {
    override def isDevelop: Boolean = !isStaging

    override def isStaging: Boolean = true

    override def isProd: Boolean = !isStaging
  }

  final case object Production extends AppEnv {
    override def isDevelop: Boolean = !isProd

    override def isStaging: Boolean = !isProd

    override def isProd: Boolean = true
  }

  val values: immutable.IndexedSeq[AppEnv] = findValues
}
