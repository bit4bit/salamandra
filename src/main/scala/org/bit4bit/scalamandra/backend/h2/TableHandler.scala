package org.bit4bit.scalamandra.backend.h2

import org.bit4bit.scalamandra.backend

class TableHandler(val table_name: String) extends backend.TableHandler {

  Database.create_table(table_name)

  def table_exists(): Boolean = false
  def add_column(column_name: String, column_type: String) = {
    Database.add_column(table_name, column_name, column_type)
  }
}
