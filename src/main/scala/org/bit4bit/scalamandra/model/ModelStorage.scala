package org.bit4bit.scalamandra.model

import org.bit4bit.scalamandra.backend

trait ModelStorage[A <: Model] extends Model {


  // MACHETE(bit4bit) como no requerir esto para instanciar la clase
  // del companion object?
  def make(): A

  def create(vlist: Seq[Map[String, Any]]): Seq[A] = {
    val parentSchema = schema

    vlist.map { values =>
      val model = make()

      // toda entidad lleva un identificador
      model.schema.ID("id", default = 0)

      // actualizar desde el schema del companion object
      model.schema.updateFromSchema(parentSchema)

      // asignar valores a campos
      for((key, value) <- values) {
        model.field(key).value = value
      }

      model
    }
  }


  def create_model(values: Map[String, Any]): A = {
    val parentSchema = schema

    val model = make()

    // toda entidad lleva un identificador
    schema.ID("id", default = 0)

    // actualizar desde el schema del companion object
    model.schema.updateFromSchema(parentSchema)

    model.field("id").value = values("id") match {
      case value: Integer => value.toLong
      case _ =>
        throw new Exception("unknown how to handle type of id")
    }

    val nvalues = values - "id"
    // asignar valores a campos
    for((key, value) <- nvalues) {
      model.field(key).value = value
    }

    model
  }
}

abstract class ModelSQL[A <: Model] extends ModelStorage[A]
    with ModelPooleable {
  this: Model =>

  implicit val database = backend.h2.Database

  def table_name: String = this.getClass.getName
  def table_handler(): backend.TableHandler = {
    // TODO(bit4bit) IoC
    new backend.h2.TableHandler(table_name, database)
  }

  override def register(model_name: String) = {
    val table = table_handler()

    table.create_table()

    // create columns on table
    this.schema.fields.foreach{ case (name, field) => table.add_column(name, field.sql_type) }
  }

  override def create(vlist: Seq[Map[String, Any]]): Seq[A] = {
    val table = table_handler()

    val ids = table.create_records(vlist)
    val vrecords: Seq[Map[String, Any]] = vlist.zipWithIndex.map{ case (values, index) =>
      values + ("id" -> ids(index))
    }

    super.create(vrecords)
  }

  def one_by_field(name: String, value: Any): Option[A] = {
    val table = table_handler()

    val vlist = table.find_by_field(name, value, limit = 1)
    if (vlist.length > 0)
      Some(super.create_model(vlist(0)))
    else
      None
  }
}
