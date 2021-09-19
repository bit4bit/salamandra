package org.bit4bit.scalamandra

import org.scalatest._

class ScalamandraPoolSpec extends TestCaseSpec {

  it should "register and access model" in {
    val db = backend.DatabaseFactory.getDumb()

    object PersonPool extends model.Model {
      schema.Char("name", default = "Yeah")
      schema.Int("age", default = 16)

      def login = true

      override def register(model_name: String)(implicit db: backend.Database): Unit = {
      }
    }
    pool.Pool.register("test.person", PersonPool)(db)
    Thread.sleep(100)

    val person = pool.Pool.get[PersonPool.type]("test.person")
    assert(person == PersonPool)
    assert(person.field("name").value == "Yeah")
    assert(person.login == true)
  }
}
