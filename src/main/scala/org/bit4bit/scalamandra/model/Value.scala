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
