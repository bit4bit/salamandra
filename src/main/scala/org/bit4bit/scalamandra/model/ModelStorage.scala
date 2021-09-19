package org.bit4bit.scalamandra.model

import org.bit4bit.scalamandra.backend

trait ModelStorage[A <: Model] extends Model {


  // MACHETE(bit4bit) como no requerir esto para instanciar la clase
  // del companion object?
  def make(): A

  def populate_model(vlist: Seq[Map[String, Any]]): Seq[A] = {
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
