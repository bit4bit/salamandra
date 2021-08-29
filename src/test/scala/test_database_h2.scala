package org.bit4bit.scalamandra

import org.scalatest._

class TestDatabaseH2 extends TestCaseSpec {
  it should "read scheme from table" in {
    implicit val db = backend.h2.Database

    class PersonH2 extends model.Model {
    }
    object PersonH2 extends model.ModelSQL[PersonH2]
    with model.Model {

      def make(): PersonH2 = new PersonH2()
      override def table_name = "test_person_H2"

      schema.Char("name", default = "Hoe")
      schema.Int("age", default = 10)
    }



    pool.Pool.register("test.person.h2", PersonH2)
    val table = PersonH2.table_handler()
    val columns = table.column_definitions()
    assert(columns("name").type_name == "varchar")
    assert(columns("name").size == 255)
    assert(columns("age").type_name == "integer")
    assert(columns("age").size == 10)
  }

  it should "create record" in {
    implicit val db = backend.h2.Database

    class PersonH2 extends model.Model {
    }
    object PersonH2 extends model.ModelSQL[PersonH2]
        with model.Model {

      def make(): PersonH2 = new PersonH2()
      override def table_name = "test_person_H2_create"

      schema.Char("name", default = "Hoe")
      schema.Int("age", default = 10)
    }

    pool.Pool.register("test.person.h2.create", PersonH2)
    val table = PersonH2.table_handler()

    val records = table.create_records(Seq(
      Map("name" -> "bob", "age" -> 16),
      Map("name" -> "dylan", "age" -> 18)
    ))

    assert(records(0) > 0)
    assert(records(1) > 0)
  }
}
