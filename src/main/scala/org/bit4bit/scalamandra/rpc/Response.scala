package org.bit4bit.scalamandra.rpc

case class RPCResponseCommonServerList(names: Seq[String]) {
  def asJson(): String = {
    ujson.Obj(
      "id" -> 1,
      "result" -> names
    ).toString
  }
}

case class RPCResponseCommonServerVersion(version: String) {
  def asJson(): String = {
    ujson.Obj(
      "id" -> 1,
      "result" -> version
    ).toString
  }
}
