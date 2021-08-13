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
