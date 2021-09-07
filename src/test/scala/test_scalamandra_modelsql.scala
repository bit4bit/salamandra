package org.bit4bit.scalamandra

import org.scalatest._

class ScalamandraModelSQLSpec extends TestCaseSpec {
  implicit val db = new backend.h2.Database("scalamandra-test-persisted")

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
      override def table_name = "test_person_persisted"

      schema.Char("name", default = "Hoe")
      schema.Int("age", default = 10)
    }

    pool.Pool.register("test.person", Person)
    assert(Person.table_name == "test_person_persisted")

    val p = Person.create(Seq(Map("name" -> "scala")))
    assert(p(0).field("id").valueAsID > 0)
    assert(p(0).field("name").valueAsString == "scala")
  }

  it should "reads model" in {

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
      override def table_name = "test_person_read"


      schema.Char("name", default = "Hoe")
      schema.Int("age", default = 10)
    }

    pool.Pool.register("test.person.read", Person)
    assert(Person.table_name == "test_person_read")

    val p = Person.create(Seq(
      Map("name" -> "scala"),
      Map("name" -> "scala2")
    ))
    assert(p(0).field("id").valueAsID > 0)
    assert(p(0).field("name").valueAsString == "scala")

    val ps = Person.one_by_field("name", "scala2")
    ps match {
      case Some(ps) =>
        assert(ps.field("id").valueAsID == p(1).field("id").valueAsID)
        assert(ps.field("name").valueAsString == "scala2")
      case None =>
        fail("one by field")
    }
  }
}
