package org.bit4bit.scalamandra.rpc

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Map

import org.bit4bit.scalamandra.Value


class RPC {
  val handler_of = Map[String, Handler]()

  def register(handler: Handler): Unit = {
    handler.rpc_register(this)
  }

  def callback(method: String, handler: Handler): Unit = {
    handler_of(method) = handler
  }

  def execute(method: String, arguments: Seq[Value]): Value = {
    handler_of(method).rpc_handler(method, arguments)
  }
}
