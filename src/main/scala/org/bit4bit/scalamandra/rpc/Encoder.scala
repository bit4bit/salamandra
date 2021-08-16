package org.bit4bit.scalamandra.rpc

trait JSONEncoder {
  def buildObject(): JSONObject
  def mergeObject(a: JSONObject, atKey: String, b: JSONObject): Unit
}

trait JSONObject {
  def set(key: String, value: String): Unit
  def render(): String

  // MACHETE(bit4bit) el mismo dilema como usar tipos
  // y evitar el uso de Any?
  def internal(): Any
}

class JSONObjectUsingUjson(var obj: ujson.Value) extends JSONObject {
  def set(key: String, value: String) = {
    obj(key) = value
  }

  def internal(): Any = obj

  def render(): String = obj.render()
}

class JSONEncoderUsingUjson extends JSONEncoder {
  def buildObject(): JSONObject = {
    new JSONObjectUsingUjson(ujson.Obj())
  }

  def mergeObject(a: JSONObject, atKey: String, b: JSONObject): Unit = {
    a.internal().asInstanceOf[ujson.Value].obj(atKey) = b.internal().asInstanceOf[ujson.Value]
  }
}
