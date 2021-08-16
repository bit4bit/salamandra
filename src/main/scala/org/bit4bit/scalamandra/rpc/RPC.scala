package org.bit4bit.scalamandra.rpc

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Map

import org.bit4bit.scalamandra.Value
import org.bit4bit.scalamandra.model.Model

// TODO(bit4bit) como puedo usar el sistema de tipado
// para garantizar en tiempo de compilacion los tipos
// de los callbacks, algo como:
//
// object SubModel extends rpc.Handler {
// ...
// }
// val r = new RPC[model.Model]()
// r.register(SubModel)
//
// por ahora usamos a Any hacemo casting al tipo esperado
// con asInstanceOf[Type]

class RPC {
  val handler_of = Map.empty[String, Handler]

  def register(handler: Handler): Unit = {
    handler.rpc_register(this)
  }

  def callback(method: String, handler: Handler): Unit = {
    handler_of(method) = handler
  }

  def execute(method: String, arguments: Seq[Value]): Value = {
    handler_of(method).rpc_handler(method, arguments)
  }

  def executeInstance(obj: Any, method: String, arguments: Seq[Value]): Value = {
    handler_of(method).rpc_handler(obj, method, arguments)
  }
}
