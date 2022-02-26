package com.easywritten.scrum.config

// TODO String 맞냐?
final case class DbConfig(host: String, port: Int, database: String, username: String, password: String) {
  val url: String = s"jdbc:postgresql://$host:$port/$database"
}
