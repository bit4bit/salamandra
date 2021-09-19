package org.bit4bit.scalamandra

import org.scalatest._

class TestDatabaseH2 extends TestCaseSpec {

    
  it should "read scheme from table" in {
    val db = new backend.h2.Database("scalamandra-test-h2-read")

    class PersonH2 extends model.Model {
    }
    object PersonH2 extends model.ModelSQL[PersonH2]
    with model.Model {

      def make(): PersonH2 = new PersonH2()
      override def table_name = "test_person_h3"

      schema.Char("name", default = "Hoe")
      schema.Int("age", default = 10)
    }
    pool.Pool.register("test.person.h2", PersonH2)(db)

    val table = db.build_table_handler(PersonH2.table_name)
    val columns = table.column_definitions()
    assert(columns("name").type_name == "varchar")
    assert(columns("name").size == 255)
    assert(columns("age").type_name == "integer")
    assert(columns("age").size == 10)
  }

  it should "create record" in {
    val db = new backend.h2.Database("scalamandra-test-h2-create")

    class PersonH2 extends model.Model {
    }
    object PersonH2 extends model.ModelSQL[PersonH2]
        with model.Model {

      def make(): PersonH2 = new PersonH2()
      override def table_name = "test_person_H2_create"

      schema.Char("name", default = "Hoe")
      schema.Int("age", default = 10)
    }

    pool.Pool.register("test.person.h2.create", PersonH2)(db)
    val table = db.build_table_handler(PersonH2.table_name)

    val records = table.create_records(Seq(
      Map("name" -> "bob", "age" -> 16),
      Map("name" -> "dylan", "age" -> 18)
    ))

    assert(records(0) > 0)
    assert(records(1) > 0)
  }

  it should "read record" in {
    val db = new backend.h2.Database("scalamandra-test-h2-read2")

    class PersonH2 extends model.Model {
    }
    object PersonH2 extends model.ModelSQL[PersonH2]
        with model.Model {

      def make(): PersonH2 = new PersonH2()
      override def table_name = "test_person_H2_read"

      schema.Char("name", default = "Hoe")
      schema.Int("age", default = 10)
    }

    pool.Pool.register("test.person.h2.read", PersonH2)(db)
    val table = db.build_table_handler(PersonH2.table_name)

    table.create_records(Seq(
      Map("name" -> "bob", "age" -> 16),
      Map("name" -> "dylan", "age" -> 18)
    ))

    val records = table.find_by_field("name", "bob")
    println(records)
    assert(records.length > 0)
    assert(records(0)("name") == "bob")
  }
}
