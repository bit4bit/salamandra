package org.bit4bit.scalamandra.backend.dumb

import org.bit4bit.scalamandra.backend

class TableHandler(table_name: String) extends backend.TableHandler {

  def table_exists(): Boolean = ???
  def create_table(): Unit = ???

  def add_column(column_name: String, column_type: String): Unit = ???
  def column_definitions(): Map[String, backend.TableColumn] = ???
  def create_records(values: Seq[Map[String, Any]]): Seq[Long] = ???
  def find_by_field(name: String, value: Any, limit: Int = 1): Seq[Map[String, Any]] = ???
}
