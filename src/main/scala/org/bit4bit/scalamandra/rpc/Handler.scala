package org.bit4bit.scalamandra.rpc

import org.bit4bit.scalamandra.Value
import org.bit4bit.scalamandra.model.Model

trait Handler {
  def rpc_register(decl: RPC): Unit
  def rpc_handler(method: String, args: Seq[Value]): Value
  // TODO(bit4bit) como usar el sistema de tipeado
  // para evitar el Any?
  def rpc_handler(obj: Any, method: String, args: Seq[Value]): Value
}
