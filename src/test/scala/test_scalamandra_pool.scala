package org.bit4bit.scalamandra

import org.scalatest._

class ScalamandraPoolSpec extends TestCaseSpec {

  it should "register and access model" in {
    object Person extends model.Model {
      schema.Char("name", default = "Yeah")
      schema.Int("age", default = 16)

      def login = true

      override def register(model_name: String): Unit = {
      }
    }
    pool.Pool.register("test.person", Person)

    val person = pool.Pool.get[Person.type]("test.person")
    assert(person == Person)
    assert(person.field("name").value == "Yeah")
    assert(person.login == true)
  }
}
