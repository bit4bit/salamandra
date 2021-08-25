package org.bit4bit.scalamandra.backend

trait TableHandler {

  def table_exists(): Boolean
  def create_table(): Unit
  def add_column(column_name: String, column_type: String): Unit
  def column_definitions(): Map[String, TableColumn]
}
