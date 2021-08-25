package org.bit4bit.scalamandra.backend.h2

import scalikejdbc._
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import org.bit4bit.scalamandra.backend.TableColumn

// http://scalikejdbc.org/documentation/sql-interpolation.html

object Database extends org.bit4bit.scalamandra.backend.Database {
  val logger = LoggerFactory.getLogger("database")

  Class.forName("org.h2.Driver")
  ConnectionPool.singleton("jdbc:h2:mem:scalamandra", "user", "pass")
  implicit val session = AutoSession

  def create_table(table_name: String)(implicit s: DBSession = AutoSession) = {
    val query = s"CREATE TABLE ${table_name} (id SERIAL NOT NULL PRIMARY KEY)"

    logger.info(query)

    SQL(query).execute().apply()
  }

  def add_column(table_name: String, column_name: String, column_type: String): Unit = {
    val query = s"ALTER TABLE ${table_name} ADD COLUMN ${column_name} ${column_type}"

    logger.info(query)

    SQL(query).execute().apply()
  }

  def column_definitions(table_name: String): Map[String, TableColumn] = {
    SQL(s"show columns from ${table_name}").map{rs =>
      val name = rs.string("field").toLowerCase
      val notnull = rs.boolean("null") == false
      val typePattern = "([a-z]+)\\((.*?)\\)".r
      val default =  rs.stringOpt("default")
      val typePattern(type_name, size) = rs.string("type").toLowerCase

      (name,
        TableColumn(
          type_name = type_name,
          size = size.toInt,
          notnull = notnull,
          default = default
        ))
    }.list().apply().toMap
  }
}
