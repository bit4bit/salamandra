package org.bit4bit.scalamandra.rpc

case class RPCResponseCommonServerList(names: Seq[String]) {
  def toJson: ujson.Value = {
    ujson.Obj(
      "id" -> 1,
      "result" -> names
    )
  }
}

case class RPCResponseCommonServerVersion(version: String) {
  def toJson: ujson.Value = {
    ujson.Obj(
      "id" -> 1,
      "result" -> version,
    )
  }
}

case class RPCResponseSession(id: String) {
  def toJson: ujson.Value = {
    ujson.Obj(
      "id" -> 1,
      "result" -> ujson.Arr(1, id)
    )
  }
}

case class RPCResponseError(exception: String, args: Seq[Any]) {
  def toJson: ujson.Value = {
    val error_args = ujson.Arr()
    val error = ujson.Arr(exception, error_args)

    args.foreach { item =>
      item match {
        case e: String => error_args.arr.append(e)
        case e: Int => error_args.arr.append(e)
        case _ =>
          throw new RuntimeException(s"not known how to handle $item")
      }
    }

    ujson.Obj(
      "id" -> 1,
      "error" -> error
    )
  }
}
