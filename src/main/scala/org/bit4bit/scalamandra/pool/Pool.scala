package org.bit4bit.scalamandra.pool

import scala.collection.mutable.Map

import org.bit4bit.scalamandra.model.Model

object Pool {
  val model = Map.empty[String, Model]

  def get(model_name: String): Model = {
    return model(model_name)
  }
}
