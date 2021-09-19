package org.bit4bit.scalamandra.backend

object DatabaseFactory {
  val database = new h2.Database("main")

  def get(): Database = database
  def getDumb(): Database = new dumb.Database
}
