package org.bit4bit.scalamandra.rpc

trait JSONEncoder {
  def buildObject(): JSONObject
  def mergeObject(a: JSONObject, atKey: String, b: JSONObject): Unit
}

trait JSONObject {
  def set(key: String, value: String): Unit
  def render(): String
}

class JSONObjectUsingUjson(var obj: ujson.Value) extends JSONObject {
  def set(key: String, value: String) = {
    obj(key) = value
  }

  def render(): String = obj.render()
}

class JSONEncoderUsingUjson extends JSONEncoder {
  def buildObject(): JSONObject = {
    new JSONObjectUsingUjson(ujson.Obj())
  }

  def mergeObject(a: JSONObject, atKey: String, b: JSONObject): Unit = {
    // TODO(bit4bit) casteo dinamico, como verificarlo estaticamente?
    a.asInstanceOf[JSONObjectUsingUjson].obj(atKey) = b.asInstanceOf[JSONObjectUsingUjson].obj
  }
}
