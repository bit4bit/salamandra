package org.bit4bit.scalamandra.pool

import scala.collection.mutable.Map

import org.bit4bit.scalamandra.model.Model

object Pool {
  val models = Map.empty[String, Model]

  def register[T <: Model](model_name: String, model: T): Unit = {
    models(model_name) = model

    model.register(model_name)
  }

  def get[T <: Model](model_name: String): T = {
    return models(model_name).asInstanceOf[T]
  }
}
