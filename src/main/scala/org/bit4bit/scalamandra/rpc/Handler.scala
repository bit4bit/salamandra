package org.bit4bit.scalamandra.rpc

import org.bit4bit.scalamandra.Value

trait Handler {
  def rpc_register(decl: RPC): Unit
  def rpc_handler(method: String, args: Seq[Value]): Value
}
