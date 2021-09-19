package org.bit4bit.scalamandra.backend.h2

import org.bit4bit.scalamandra.backend

class TableHandler(val table_name: String, val db: backend.Database) extends backend.TableHandler {

  def create_table() = db.create_table(table_name)

  def table_exists(): Boolean = false

  def add_column(column_name: String, column_type: String) = {
    db.add_column_if_not_exists(table_name, column_name, column_type)
  }

  def column_definitions(): Map[String, backend.TableColumn] = {
    db.column_definitions(table_name)
  }

  def create_records(vlist: Seq[Map[String, Any]]): Seq[Long] = {
    vlist.map{values =>
      db.insert(table_name, values)
    }
  }

  def find_by_field(name: String, value: Any, limit: Int): Seq[Map[String, Any]] = {
    db.select_all(s"SELECT * FROM ${table_name} WHERE ${name} = ? LIMIT ${limit}", List(value))
  }
}
