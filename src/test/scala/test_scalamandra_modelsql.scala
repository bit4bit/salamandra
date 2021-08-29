package org.bit4bit.scalamandra

import org.scalatest._

class ScalamandraModelSQLSpec extends TestCaseSpec {
  it should "create model" in {
    class Person extends model.Model {
      def newAge(): Int = {
        field("age").valueAsInt + 10
      }

      def setAge(age: Int): Unit = {
        field("age").value = age
      }
    }
    object Person extends model.Model with model.ModelStorage[Person] {
      def make(): Person = new Person()

      schema.Char("name", default = "Hoe")
      schema.Int("age", default = 10)
    }
    
    val p = Person.create(Seq(Map("name" -> "scala")))
    assert(p(0).field("id").value == 0, "has id field")
    assert(p(0).field("name").value == "scala")
    assert(p(0).field("age").value == 10)

    assert(p(0).newAge() == 20)
    p(0).setAge(33)
    assert(p(0).field("age").value == 33)
  }

  it should "create model persisted" in {
    implicit val db = backend.h2.Database

    class Person extends model.Model {
      def newAge(): Int = {
        field("age").valueAsInt + 10
      }

      def setAge(age: Int): Unit = {
        field("age").value = age
      }
    }
    object Person extends model.ModelSQL[Person] {

      def make(): Person = new Person()
      override def table_name = "test_person"

      schema.Char("name", default = "Hoe")
      schema.Int("age", default = 10)
    }

    pool.Pool.register("test.person", Person)
    assert(Person.table_name == "test_person")

    val p = Person.create(Seq(Map("name" -> "scala")))
    assert(p(0).field("id").valueAsID > 0)
    assert(p(0).field("name").valueAsString == "scala")
  }
}
