package org.bit4bit.scalamandra.backend.h2

import org.bit4bit.scalamandra.backend

class TableHandler(val table_name: String, val db: backend.Database) extends backend.TableHandler {

  def create_table() = Database.create_table(table_name)

  def table_exists(): Boolean = false

  def add_column(column_name: String, column_type: String) = {
    db.add_column(table_name, column_name, column_type)
  }

  def column_definitions(): Map[String, backend.TableColumn] = {
    db.column_definitions(table_name)
  }
}
