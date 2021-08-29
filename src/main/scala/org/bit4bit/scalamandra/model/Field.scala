package org.bit4bit.scalamandra.model

import org.bit4bit.scalamandra.Value

trait Field {
  type VALUE

  def sql_type: String
  // access and update internal value
  def value: VALUE
  def value_=(v: Any): Unit

  def valueAsInt: Int = {
    value match {
      case v: Int => v
      case _ =>
        throw new IllegalArgumentException("can't handle type")
    }
  }

  def valueAsID: Long = {
    value match {
      case v: Long => v
      case _ =>
        throw new IllegalArgumentException("can't handle type")
    }
  }

  def valueAsString: String = {
    value match {
      case v: String => v
      case _ =>
        throw new IllegalArgumentException("can't handle type")
    }
  }

  def initial_value(): Value

  def definition(): Field.Definition

  def copy(): Field
}

object Field {
  //https://hg.tryton.org/trytond/file/tip/trytond/model/fields/field.py#l436
  // * name: in python is class attribute name
  case class Definition(name: String, _type: String) {
  }

  case class Char(name: String, default: String) extends Field {
    type VALUE = String

    var internal: Value = Value.Str(default)

    def sql_type = "VARCHAR(255)"

    def value = internal.str
    def value_=(v: Any): Unit = {
      v match {
        case vs: String =>
          internal = Value.Str(vs)
        case _ =>
          throw new IllegalArgumentException("can't handle type")
      }
    }
    
    override def initial_value(): Value = {
      Value.Str(default)
    }

    override def definition(): Definition = {
      Definition(name = name, _type = "char")
    }

    def copy(): Char = new Char(name, default = default)
  }

  case class Int(name: String, default: Integer) extends Field {
    type VALUE = Integer

    private var internal: Value = Value.Int(default)

    def sql_type = "INTEGER"

    def value = internal.int
    def value_=(v: Any): Unit = {
      v match {
        case vs: Integer =>
          internal = Value.Int(vs)
        case _ =>
          throw new IllegalArgumentException("can't handle type")
      }
    }

    override def initial_value(): Value = {
      Value.Int(default)
    }

    override def definition(): Definition = {
      Definition(name = name, _type = "int")
    }

    def copy(): Int = Int(name, default = default)
  }

  case class ID(name: String, default: Long) extends Field {
    type VALUE = Long

    private var internal: Value = Value.ID(default)

    def sql_type = "BIGINT"

    def value = internal.id
    def value_=(v: Any): Unit = {
      v match {
        case vs: Long =>
          internal = Value.ID(vs)
        case _ =>
          throw new IllegalArgumentException("can't handle type")
      }
    }

    override def initial_value(): Value = {
      Value.ID(default)
    }

    override def definition(): Definition = {
      Definition(name = name, _type = "int")
    }

    def copy(): ID = ID(name, default = default)
  }
}
