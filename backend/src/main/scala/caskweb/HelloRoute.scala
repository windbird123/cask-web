package caskweb

case class HelloRoute() extends cask.Routes {
  @cask.get("/hello")
  def hello() = "Hello World!"

  initialize()
}
