package org.bit4bit.scalamandra.pool

import scala.collection.mutable.Map

import org.bit4bit.scalamandra.model.Model

object Pool {
  val model = Map.empty[String, Model]

  def get[T <: Model](model_name: String): T = {
    return model(model_name).asInstanceOf[T]
  }
}
