package org.bit4bit.scalamandra

import org.scalatest._

class ScalamandraRPCSpec extends TestCaseSpec {

  it should "declare and dispatch rpc functions" in {
    case class CalcVal(num: Int) extends model.Model {
        def scheme(schema: org.bit4bit.scalamandra.model.Schema): Unit = {}
    }

    object Calc extends rpc.Handler {
      def rpc_register(decl: rpc.RPC): Unit = {
        decl.callback("sum", this)
      }

      def rpc_handler(method: String, args: Seq[rpc.Argument]): rpc.Reply = {
        method match {
          case "sum" =>
            return rpc.Reply.Int((args(0).int + args(1).int))
        }
      }

      def rpc_handler(obj: Any, method: String, args: Seq[rpc.Argument]): rpc.Reply = {
        method match {
          case "sum" =>
            return rpc.Reply.Int(obj.asInstanceOf[CalcVal].num + args(0).int)
        }
      }
    }

    val srv = new rpc.RPC()
    srv.register(Calc)

    assert(srv.execute("sum", Seq(rpc.Argument.Int(30), rpc.Argument.Int(16))).int == 46)

    val c = CalcVal(33)
    assert(c.num == 33)
    assert(srv.executeInstance(c, "sum", Seq(rpc.Argument.Int(30))).int == 63)
  }

  it should "render definition fields" in {
    object Person extends model.Model {
      schema.Char("bobname", default = "Yeah")
    }

    val srv = new rpc.RPC()
    srv.register(Person)

    val encoder = new rpc.JSONEncoderUsingUjson()

    assert(srv.execute("fields_get", Seq(
      rpc.Argument.Arr(
        Seq(rpc.Argument.Str("bobname"))
      )
    )
    ).json(encoder) == """{"bobname":{"name":"bobname","type":"char"}}""")
  }
}
