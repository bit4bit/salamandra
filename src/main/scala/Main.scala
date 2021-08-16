package app

import org.bit4bit.scalamandra.rpc._

object ScalamandraApplication extends cask.MainRoutes {
  @cask.route("/:path", methods = Seq("options"))
  def options(path: String) = {
    cask.Response("", statusCode = 204)
  }

  @cask.postJson("/")
  def root(method: String, params: ujson.Value = Seq()) = {
    method match {
      case "common.server.list" =>
        RPCResponseCommonServerList(Seq("scalamandra")).toJson
      case "common.server.version" =>
        RPCResponseCommonServerVersion("0.1.0").toJson
    }
  }

  initialize()
}
