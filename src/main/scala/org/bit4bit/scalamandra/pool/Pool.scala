package org.bit4bit.scalamandra.pool

import scala.collection.mutable.Map

import org.bit4bit.scalamandra.model
import org.bit4bit.scalamandra.backend

object Pool {
  val models = Map.empty[String, model.Model]

  def register[T <: model.Model](model_name: String, model_local: T)(implicit db: backend.Database): Unit = {
    models(model_name) = model_local

    model_local.register(model_name)
  }

  def get[T <: model.Model](model_name: String): T = {
    return models(model_name).asInstanceOf[T]
  }
}
