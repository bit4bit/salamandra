package org.bit4bit.scalamandra.backend.h2

import scalikejdbc._
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import org.bit4bit.scalamandra.backend.TableColumn

// http://scalikejdbc.org/documentation/sql-interpolation.html
class Database(name: String) extends org.bit4bit.scalamandra.backend.Database {
  val logger = LoggerFactory.getLogger(s"database:${name}")

  Class.forName("org.h2.Driver")
  ConnectionPool.singleton(s"jdbc:h2:mem:${name}", "user", "pass")
  implicit val session = AutoSession

  def name(): String = s"jdbc:h2:mem:${name}"

  def build_table_handler(table_name: String): org.bit4bit.scalamandra.backend.TableHandler = {
    new TableHandler(table_name, this)
  }

  def create_table(table_name: String): Unit = {
    val query = s"CREATE TABLE IF NOT EXISTS ${table_name} (id SERIAL NOT NULL PRIMARY KEY)"

    logger.info(query)

    SQL(query).execute().apply()
  }

  def add_column_if_not_exists(table_name: String, column_name: String, column_type: String): Unit = {
    val query = s"ALTER TABLE ${table_name} ADD COLUMN ${column_name} ${column_type}"

    logger.info(query)
    if (!(column_definitions(table_name) contains column_name)) {
      SQL(query).execute().apply()
    }
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

  def insert(table_name: String, values: Map[String, Any]): Long = {
    // concatenar columnas -> ${a},$
    val columns = values.keys.map(k => s"${k}").mkString(",")
    val placeholders = List.fill(values.keys.size)("?").mkString(",")
    val parameters = values.values.toSeq
    val query = s"INSERT INTO ${table_name} (${columns}) VALUES(${placeholders})"

    SQL(query).bind(parameters: _*).updateAndReturnGeneratedKey().apply()
  }

  def select_all(query: String, parameters: Seq[Any]): Seq[Map[String, Any]] = {
    SQL(query).bind(parameters: _*).map(_.toMap()).list().apply().map{ row =>
      row.map{ case (k, v) => k.toLowerCase -> v }
    }
  }
}
