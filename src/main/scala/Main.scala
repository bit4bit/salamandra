package app

object ScalamandraApplication extends cask.MainRoutes {
  @cask.route("/:path", methods = Seq("options"))
  def options(path: String) = {
    cask.Response("", statusCode = 204)
  }

  initialize()
}
