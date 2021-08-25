package org.bit4bit.scalamandra.model

import org.bit4bit.scalamandra.backend

trait ModelStorage[A <: Model] {
  this: Model =>

  // MACHETE(bit4bit) como no requerir esto para instanciar la clase
  // del companion object?
  def make(): A

  def create(vlist: Seq[Map[String, Any]]): Seq[A] = {
    val parentSchema = schema

    vlist.map { values =>
      val model = make()

      // toda entidad lleva un identificador
      model.schema.Int("id", default = 0)

      // actualizar desde el schema del companion object
      model.schema.updateFromSchema(parentSchema)

      // asignar valores a campos
      for((key, value) <- values) {
        model.field(key).value = value
      }

      model
    }
  }
}

trait ModelSQL[A <: Model] extends ModelPooleable
    with ModelStorage[A] {

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
    this.schema.fields.foreach{ case (name, field) => table.add_column(name, field.sql_type)}

  }
}
