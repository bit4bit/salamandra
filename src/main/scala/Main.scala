package app

object ScalamandraApplication extends cask.MainRoutes {
  @cask.route("/:path", methods = Seq("options"))
  def options(path: String) = {
    cask.Response("", statusCode = 204)
  }

  @cask.postJson("/")
  def root(method: String, params: ujson.Value = Seq()) = {
    method match {
      case "common.server.list" =>
        ujson.Obj(
          "id" -> 1,
          "result" -> Seq("scalamandra")
        )
      case "common.server.version" =>
        ujson.Obj(
          "id" -> 1,
          "result" -> Seq("0.1.0")
        )
    }
  }

  initialize()
}
