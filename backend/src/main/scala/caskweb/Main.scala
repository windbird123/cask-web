package caskweb

case class MinimalRoutes()(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes {
  @cask.get("/hello")
  def hello() =
    "Hello World!"

  @cask.get("/")
  def index() =
    cask.StaticResource(
      "dist/index.html",
      getClass.getClassLoader,
      List("Content-Type" -> "text/html; charset=utf-8")
    )

  @StaticResourcesWithContentType("/assets")
  def assets() = "dist/assets"

  @StaticResourcesWithContentType("/static")
  def static() = "dist/static"

  initialize()
}

object Main extends cask.Main {
  println("START server")
  override def host: String = "0.0.0.0"

  override def port: Int = 8080

  val allRoutes = Seq(MinimalRoutes())
}
