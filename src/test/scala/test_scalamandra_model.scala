package org.bit4bit.scalamandra

import org.scalatest._
import io.undertow.Undertow

class ScalamandraModelSpec extends TestCaseSpec with BeforeAndAfter {

  it should "declare fields" in {
    object Person extends model.Model {
      schema.Char("name", default = "Yeah")
      schema.Int("age", default = 16)
    }


    val defaults = Person.defaults_get(Seq("name", "age"))
    assert(defaults("name").str == "Yeah")
    assert(defaults("age").int == 16)
  }

  it should "access fields" in {
    object Person extends model.Model {
      schema.Char("name", default = "Yeah")
    }

    assert(Person.field("name").value == "Yeah")
    Person.field("name").value = "Bob"
    assert(Person.field("name").value == "Bob")
  }

  it should "definition fields" in {
    object Person extends model.Model {
      schema.Char("name", default = "Yeah")
    }

    val definitions = Person.fields_get(Seq("name"))
    assert(definitions("name").name == "name")
    assert(definitions("name")._type == "char")
  }
}
