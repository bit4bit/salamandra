package app

import org.bit4bit.scalamandra.rpc._

object ScalamandraApplication extends cask.MainRoutes {
  @cask.postJson("/:database_name/")
  def rpc(database_name: String, id: Int, method: String, params: ujson.Value) = {
    method match {
      case "common.db.login" =>
        val username = params.arr(0).str
        val device_cookie = params.arr(1)("device_cookie").str
        var password = ""
        try {
          password = params.arr(1)("password").str.trim()
        } catch {
          case e: java.util.NoSuchElementException => password = ""
        }

        login(database_name, username, device_cookie, password)
    }
  }

  @cask.route("/:path", methods = Seq("options"))
  def options(path: String) = {
    cask.Response("", statusCode = 204)
  }

  @cask.postJson("/")
  def root(id: Int, method: String, params: ujson.Value = Seq()) = {
    method match {
      case "common.server.list" =>
        RPCResponseCommonServerList(Seq("scalamandra")).toJson
      case "common.server.version" =>
        RPCResponseCommonServerVersion("0.1.0").toJson
    }
  }

  def login(database_name: String, username: String, device_cookie: String, password: String) = {
    if (password == "") {
      RPCResponseError("LoginException", "password", "Password for admin", "password").toJson
    } else {
      val session_id = "9759ea66a364c308b21a7ac5c3b0ea68cc98b6675034cbb21d4d78fc6736bf14"
      RPCResponseSession(session_id).toJson
    }
  }

  initialize()
}
