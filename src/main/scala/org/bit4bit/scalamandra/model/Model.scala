package org.bit4bit.scalamandra.model

sealed trait Value {
  def str = this match {
    case Value.Str(value) => value
    case _ => throw Value.InvalidData(this, "Expected Value.Str")
  }

  def int = this match {
    case Value.Int(value) => value
    case _ => throw Value.InvalidData(this, "Expected Value.Int")
  }
}

object Value {
  // taken from ujson
  case class InvalidData(data: Value, msg: String)
      extends Exception(s"$msg (data: $data)")
  case class Str(value: String) extends Value
  case class Int(value: Integer) extends Value
}


trait Model {
  val schema = new Schema()

  scheme(schema)

  def scheme(schema: Schema): Unit

  def defaults_get(fieldsNames: Seq[String]): Map[String, Value] = {
    schema.fields.map { case (name, field) =>
      (name, field.initial_value())
    }.filter { case (name, field) =>
        fieldsNames contains name
    }.toMap
  }
}

