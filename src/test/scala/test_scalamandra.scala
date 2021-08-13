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
      data = ujson.Obj("method" -> "common.server.version", "params" -> Seq()),
      headers = Map(
        "Content-Type" -> "application/json"
      )
    )

    val data = ujson.read(resp.text())
    assert(data("id").num == 1)
    assert(data("result").arr(0).str == "0.1.0")
  }

  it should "respond method common.server.list" in {
    val resp = requests.post("http://localhost:8099/",
      data = ujson.Obj("method" -> "common.server.list", "params" -> Seq()),
      headers = Map(
        "Content-Type" -> "application/json"
      )
    )

    val data = ujson.read(resp.text())
    assert(data("id").num == 1)
    assert(data("result").arr(0).str == "scalamandra")
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
