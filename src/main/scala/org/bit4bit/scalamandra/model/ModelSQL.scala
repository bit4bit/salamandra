package org.bit4bit.scalamandra.model

import org.slf4j.LoggerFactory

import org.bit4bit.scalamandra.backend

abstract class ModelSQL[A <: Model] extends ModelStorage[A] {
  this: Model =>
  val logger = LoggerFactory.getLogger("model-sql")

  def table_name: String = this.getClass.getName

  override def register(model_name: String)(implicit db: backend.Database) = {
    val table = db.build_table_handler(table_name)

    table.create_table()

    // create columns on table
    this.schema.fields.foreach{ case (name, field) => table.add_column(name, field.sql_type) }
  }

  def create(vlist: Seq[Map[String, Any]])(implicit db: backend.Database): Seq[A] = {
    val table = db.build_table_handler(table_name)

    val ids = table.create_records(vlist)
    val vrecords: Seq[Map[String, Any]] = vlist.zipWithIndex.map{ case (values, index) =>
      values + ("id" -> ids(index))
    }

    super.populate_model(vrecords)
  }

  def one_by_field(name: String, value: Any)(implicit db: backend.Database): Option[A] = {
    val table = db.build_table_handler(table_name)

    val vlist = table.find_by_field(name, value, limit = 1)
    if (vlist.length > 0)
      Some(super.create_model(vlist(0)))
    else
      None
  }
}
