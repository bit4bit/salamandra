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
      "result" -> Seq(version),
    )
  }
}
