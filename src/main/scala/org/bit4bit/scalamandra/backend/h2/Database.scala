package org.bit4bit.scalamandra.backend.h2

import scalikejdbc._
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Database {
  val logger = LoggerFactory.getLogger("database")

  Class.forName("org.h2.Driver")
  ConnectionPool.singleton("jdbc:h2:mem:scalamandra", "user", "pass")
  implicit val session = AutoSession

  def create_table(table_name: String)(implicit s: DBSession = AutoSession) = {
    val query = s"CREATE TABLE ${table_name} (id SERIAL NOT NULL PRIMARY KEY)"

    logger.info(query)

    SQL(query).execute().apply()
  }

  def add_column(table_name: String, column_name: String, column_type: String)(implicit s: DBSession = AutoSession) = {
    val query = s"ALTER TABLE ${table_name} ADD COLUMN ${column_name} ${column_type}"

    logger.info(query)

    SQL(query).execute().apply()
  }
}
