package org.bit4bit.scalamandra.backend

trait TableHandler {

  def table_exists(): Boolean
  def add_column(column_name: String, column_type: String): Unit
}
