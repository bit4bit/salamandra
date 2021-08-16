package org.bit4bit.scalamandra.model

import org.bit4bit.scalamandra.Value
import org.bit4bit.scalamandra.rpc

class Model {
  val schema = new Schema()

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

  def field(name: String): Field = {
    schema.fields(name)
  }

  def value(name: String): Value = {
    return schema.fields(name).value
  }
}

object Model extends Model with rpc.Handler  {
  def rpc_register(decl: rpc.RPC): Unit = {
    decl.callback("fields_get", this)
    decl.callback("default_get", this)
  }

  def rpc_handler(method: String, args: Seq[Value]): Value = {
    Value.Int(0)
  }
  def rpc_handler(obj: Any, method: String, args: Seq[Value]): Value = {
    Value.Int(0)
  }
}
