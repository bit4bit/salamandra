package org.bit4bit.scalamandra.model

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Map

trait Field {
  def initial_value(): Value

  def definition(): Field.Definition
}

object Field {
  //https://hg.tryton.org/trytond/file/tip/trytond/model/fields/field.py#l436
  case class Definition(name: String, _type: String) {
  }

  case class Char(name: String, default: String) extends Field {
    override def initial_value(): Value = {
      Value.Str(default)
    }

    override def definition(): Definition = {
      Definition(name = name, _type = "char")
    }
  }

  case class Int(name: String, default: Integer) extends Field {
    override def initial_value(): Value = {
      Value.Int(default)
    }

    override def definition(): Definition = {
      Definition(name = name, _type = "int")
    }
  }
}

// declarar campos del modelo
class Schema {
  val fields = Map[String, Field]()

  def Char(name: String, default: String = ""): Unit = {
    fields(name) = Field.Char(name = name, default = default)
  }

  def Int(name: String, default: Integer = 0): Unit = {
    fields(name) = Field.Int(name = name, default = default)
  }
}
