package org.bit4bit.scalamandra

import org.scalatest._
import io.undertow.Undertow

class ScalamandraSpec extends TestCaseSpec with BeforeAndAfter {
  val server = Undertow.builder
    .addHttpListener(8099, "localhost")
    .setHandler(app.ScalamandraApplication.defaultHandler)
    .build

  before {
    server.start()
  }

  after {
    server.stop()
  }

  it should "respond method common.server.version" in {

    val resp = requests.post("http://localhost:8099/",
      data = ujson.Obj("id" -> 1, "method" -> "common.server.version", "params" -> Seq()),
      headers = Map(
        "Content-Type" -> "application/json"
      )
    )

    val data = ujson.read(resp.text())
    assert(data("id").num == 1)
    assert(data("result").str == "0.1.0")
  }

  it should "respond method common.server.list" in {

    val resp = requests.post("http://localhost:8099/",
      data = ujson.Obj("id" -> 1, "method" -> "common.server.list", "params" -> Seq()),
      headers = Map(
        "Content-Type" -> "application/json"
      )
    )

    val data = ujson.read(resp.text())
    assert(data("id").num == 1)
    assert(data("result").arr(0).str == "scalamandra")
  }

  it should "respond method common.db.login first phase" in {
    val resp = requests.post("http://localhost:8099/test/",
      data = ujson.Obj(
        "id" -> 1,
        "method" -> "common.db.login",
        "params" -> ujson.Arr("admin", ujson.Obj("device_cookie" -> "fa3d23cb377643d8ae7c90bef4c025cb"), "en")
      ),
      headers = Map(
        "Content-Type" -> "application/json"
      )
    )

    val data: ujson.Value = ujson.read(resp.text())
    assert(data("id").num == 1)
    assert(data("error").arr(0).str == "LoginException")
    // name
    assert(data("error").arr(1).arr(0).str == "password")
    // type
    assert(data("error").arr(1).arr(2).str == "password")
  }

  it should "respond method common.db.login complete phase" in {
    val resp = requests.post("http://localhost:8099/test/",
      data = ujson.Obj(
        "id" -> 1,
        "method" -> "common.db.login",
        "params" -> ujson.Arr("admin", ujson.Obj("device_cookie" -> "fa3d23cb377643d8ae7c90bef4c025cb"), "en")
      ),
      headers = Map(
        "Content-Type" -> "application/json"
      )
    )

    val data: ujson.Value = ujson.read(resp.text())
    assert(data("id").num == 1)
    assert(data("error").arr(0).str == "LoginException")


    val resp2 = requests.post("http://localhost:8099/test/",
      data = ujson.Obj(
        "id" -> 1,
        "method" -> "common.db.login",
        "params" -> ujson.Arr("admin", ujson.Obj("device_cookie" -> "fa3d23cb377643d8ae7c90bef4c025cb", "password" -> "admin"), "en")
      ),
      headers = Map(
        "Content-Type" -> "application/json"
      )
    )

    val data2: ujson.Value = ujson.read(resp2.text())
    assert(data2("id").num == 1)
    assert(data2("result").arr(0).num == 1)
    assert(data2("result").arr(1).str != "")
  }

  it should "respond options" in {
    val urls = Seq(
      "/test",
    )

    for (path <- urls) {
      val success = requests.options(s"http://localhost:8099$path")
      assert(success.statusCode == 204)
    }

  }
}
