package org.bit4bit.scalamandra.backend

case class TableColumn(type_name: String, notnull: Boolean, size: Integer, default: Option[String])


trait Database {
  def create_table(table_name: String): Unit
  def add_column(table_name: String, column_name: String, column_type: String): Unit
  def column_definitions(table_name: String): Map[String, TableColumn]
  def insert(table_name: String, values: Map[String, Any]): Long
  def select_all(query: String, values: Seq[Any]): Seq[Map[String, Any]]
}
