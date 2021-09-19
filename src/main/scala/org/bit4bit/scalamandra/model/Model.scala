package org.bit4bit.scalamandra.model

import org.bit4bit.scalamandra.Value
import org.bit4bit.scalamandra.rpc
import org.bit4bit.scalamandra.backend

trait Model extends rpc.Handler {
  val schema = new Schema()

  def register(model_name: String)(implicit db: backend.Database): Unit = ???

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

  def rpc_register(decl: rpc.RPC): Unit = {
    decl.callback("fields_get", this)
  }

  def rpc_handler(method: String, args: Seq[rpc.Argument]): rpc.Reply = {
    method match {
      case "fields_get" =>
        val fargs: Seq[String] = args(0).arr.map { argument => argument.str }
        return Model.FieldsDefinitionsReply(this.fields_get(fargs))
    }
  }

  def rpc_handler(obj: Any, method: String, args: Seq[rpc.Argument]): rpc.Reply = {
    rpc.Reply.Empty()
  }
}



object Model extends Model {
  case class FieldsDefinitionsReply(definitions: Map[String, Field.Definition]) extends rpc.Reply {
    def json(encoder: rpc.JSONEncoder): String = {
      val obj = encoder.buildObject()

      definitions.foreach{ case (key, definition) =>
        val dobj = encoder.buildObject()
        dobj.set("name", definition.name)
        dobj.set("type", definition._type)
        encoder.mergeObject(obj, key, dobj)
      }

      obj.render()
    }
  }
}
