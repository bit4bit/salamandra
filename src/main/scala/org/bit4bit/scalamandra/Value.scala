package org.bit4bit.scalamandra

trait Value {
  def str = this match {
    case Value.Str(value) => value
    case _ => throw Value.InvalidData(this, "Expected Value.Str")
  }

  def int = this match {
    case Value.Int(value) => value
    case _ => throw Value.InvalidData(this, "Expected Value.Int")
  }

  def id = this match {
    case Value.ID(value) => value
    case _ => throw Value.InvalidData(this, "Expected Value.ID")
  }
}

object Value {
  // taken from ujson
  case class InvalidData(data: Value, msg: String)
      extends Exception(s"$msg (data: $data)")
  
  case class Str(value: String) extends Value
  case class Int(value: Integer) extends Value
  case class ID(value: Long) extends Value
}
