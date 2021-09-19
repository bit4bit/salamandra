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

    val p = Person.populate_model(Seq(Map("name" -> "scala")))
    assert(p(0).field("id").value == 0, "has id field")
    assert(p(0).field("name").value == "scala")
    assert(p(0).field("age").value == 10)

    assert(p(0).newAge() == 20)
    p(0).setAge(33)
    assert(p(0).field("age").value == 33)
  }

  it should "create model persisted" in {
    val db = new backend.h2.Database("scalamandra-test-persisted2")

    class PersonCreate extends model.Model {
      def newAge(): Int = {
        field("age").valueAsInt + 10
      }

      def setAge(age: Int): Unit = {
        field("age").value = age
      }
    }
    object PersonCreate extends model.ModelSQL[PersonCreate] {

      def make(): PersonCreate = new PersonCreate()
      override def table_name = "test_person_persisted"

      schema.Char("name", default = "Hoe")
      schema.Int("age", default = 10)
    }
    pool.Pool.register("test.person", PersonCreate)(db)
    assert(PersonCreate.table_name == "test_person_persisted")

    val p = PersonCreate.create(Seq(Map("name" -> "scala")))(db)
    assert(p(0).field("id").valueAsID > 0)
    assert(p(0).field("name").valueAsString == "scala")
  }

  it should "reads model" in {
    val db = new backend.h2.Database("scalamandra-test-persisted3")

    class PersonRead extends model.Model {
      def newAge(): Int = {
        field("age").valueAsInt + 10
      }

      def setAge(age: Int): Unit = {
        field("age").value = age
      }
    }
    object PersonRead extends model.ModelSQL[PersonRead] {

      def make(): PersonRead = new PersonRead()
      override def table_name = "test_person_read"

      schema.Char("name", default = "Hoe")
      schema.Int("age", default = 10)
    }

    pool.Pool.register("test.person.read", PersonRead)(db)

    assert(PersonRead.table_name == "test_person_read")

    val p = PersonRead.create(Seq(
      Map("name" -> "scala"),
      Map("name" -> "scala2")
    ))(db)
    assert(p(0).field("id").valueAsID > 0)
    assert(p(0).field("name").valueAsString == "scala")

    val ps = PersonRead.one_by_field("name", "scala2")(db)
    ps match {
      case Some(ps) =>
        assert(ps.field("id").valueAsID == p(1).field("id").valueAsID)
        assert(ps.field("name").valueAsString == "scala2")
      case None =>
        fail("one by field")
    }
  }
}
