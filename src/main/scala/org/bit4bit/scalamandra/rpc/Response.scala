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

case class RPCResponseError(exception: String, name: String, message: String, _type: String) {
  def toJson: ujson.Value = {
    ujson.Obj(
      "id" -> 1,
      "error" -> ujson.Arr(exception, ujson.Arr(name, message, _type))
    )
  }
}
