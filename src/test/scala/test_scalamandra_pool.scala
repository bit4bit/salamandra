package org.bit4bit.scalamandra

import org.scalatest._

class ScalamandraPoolSpec extends TestCaseSpec {

  it should "register and access model" in {
    object Person extends model.Model {
      schema.Char("name", default = "Yeah")
      schema.Int("age", default = 16)
    }
    pool.Pool.model("test.person") = Person

    val person = pool.Pool.get("test.person")
    assert(person == Person)
    assert(person.field("name").value.str == "Yeah")
  }
}
