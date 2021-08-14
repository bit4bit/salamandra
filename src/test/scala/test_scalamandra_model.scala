package org.bit4bit.scalamandra

import org.scalatest._
import io.undertow.Undertow

class ScalamandraModelSpec extends TestCaseSpec with BeforeAndAfter {

  it should "declare fields" in {
    class Person extends model.Model {

      override def scheme(schema: model.Schema): Unit = {
        schema.Char("name", default = "Yeah")
        schema.Int("age", default = 16)
      }
    }

    val p1 = new Person()
    val defaults = p1.defaults_get(Seq("name", "age"))
    assert(defaults("name").str == "Yeah")
    assert(defaults("age").int == 16)
  }

  it should "definition fields" in {
    class Person extends model.Model {
      override def scheme(schema: model.Schema): Unit = {
        schema.Char("name", default = "Yeah")
      }
    }

    val p = new Person()
    val definitions = p.fields_get(Seq("name"))
    assert(definitions("name").name == "name")
    assert(definitions("name")._type == "char")
  }
}
