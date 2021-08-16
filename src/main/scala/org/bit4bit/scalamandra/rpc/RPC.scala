package org.bit4bit.scalamandra.rpc

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Map

import org.bit4bit.scalamandra.Value
import org.bit4bit.scalamandra.model.Model

sealed trait Argument {
  def str = this match {
    case Argument.Str(value) => value
    case _ => throw new Exception("invalid argument")
  }

  def int = this match {
    case Argument.Int(value) => value
    case _ => throw new Exception("invalid argument")
  }

  def arr = this match {
    case Argument.Arr(args) => args
    case _ => throw new Exception("invalid argument")
  }
}

object Argument {
  case class Int(arg: Integer) extends Argument
  case class Str(arg: String) extends Argument
  case class Arr(arg: Seq[Argument]) extends Argument
}

trait Reply {
  def int = this match {
    case Reply.Int(value) => value
    case _ => throw new Exception("invalid argument")
  }

  def json(encoder: JSONEncoder): String
}

object Reply {
  case class Int(i: Integer) extends Reply {
    def json(encoder: JSONEncoder): String = i.toString()
  }
  case class Empty() extends Reply {
    def json(encoder: JSONEncoder): String = ""
  }
}


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
  type HandlerMap = scala.collection.mutable.HashMap[String, Handler]

  val handler_of: HandlerMap = new HandlerMap

  def register(handler: Handler): Unit = {
    handler.rpc_register(this)
  }

  def callback(method: String, handler: Handler): Unit = {
    handler_of(method) = handler
  }

  def execute(method: String, arguments: Seq[Argument]): Reply = {
    handler_of(method).rpc_handler(method, arguments)
  }

  def executeInstance(obj: Any, method: String, arguments: Seq[Argument]): Reply = {
    handler_of(method).rpc_handler(obj, method, arguments)
  }
}
