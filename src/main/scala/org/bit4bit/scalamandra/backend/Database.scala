package org.bit4bit.scalamandra.backend

case class TableColumn(type_name: String, notnull: Boolean, size: Integer, default: Option[String])


trait Database {
  def add_column(table_name: String, column_name: String, column_type: String): Unit
  def column_definitions(table_name: String): Map[String, TableColumn]
  def insert(table_name: String, values: Map[String, Any]): Long
}
