package org.bit4bit.scalamandra.model

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Map

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
