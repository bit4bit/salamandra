package org.bit4bit.scalamandra.backend.dumb

import org.bit4bit.scalamandra.backend

class Database extends backend.Database {
  def name() = "dumb"

  def create_table(table_name: String): Unit = ???


  def build_table_handler(table_name: String): TableHandler = ???


  def add_column_if_not_exists(table_name: String, column_name: String, column_type: String): Unit = ???

  def column_definitions(table_name: String): Map[String, backend.TableColumn] = ???

  def insert(table_name: String, values: Map[String, Any]): Long = ???
  def select_all(query: String, values: Seq[Any]): Seq[Map[String, Any]] = ???
}
