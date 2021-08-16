package org.bit4bit.scalamandra.model

import org.bit4bit.scalamandra.Value

trait Model {
  val schema = new Schema()

  scheme(schema)

  def scheme(schema: Schema): Unit

  def field(name: String): Field = {
    schema.fields(name)
  }

  def value(name: String): Value = {
    return schema.fields(name).value
  }

  def defaults_get(fieldsNames: Seq[String]): Map[String, Value] = {
    schema.fields.map { case (name, field) =>
      (name, field.initial_value())
    }.filter { case (name, field) =>
        fieldsNames contains name
    }.toMap
  }

  def fields_get(fieldsNames: Seq[String]): Map[String, Field.Definition] = {
    schema.fields.filter { case (name, field) =>
      fieldsNames contains name
    }.map { case (name,field) =>
        (name, field.definition())
    }.toMap
  }
}

