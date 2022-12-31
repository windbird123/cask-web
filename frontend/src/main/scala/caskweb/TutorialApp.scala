package caskweb

import org.scalajs.dom
import org.scalajs.dom.document
import com.raquo.laminar.api.L._

object TutorialApp {
  def appendPar(targetNode: dom.Node, text: String): Unit = {
    val parNode = document.createElement("p")
    parNode.textContent = text
    targetNode.appendChild(parNode)
  }

//  @JSExportTopLevel("addClickedMessage")
  def addClickedMessage(): Unit =
    appendPar(document.body, "You clicked the button")

  def setupUI(): Unit = {
    val button = document.createElement("button")
    button.textContent = "Click me!"
    button.addEventListener(
      "click",
      (e: dom.MouseEvent) =>
        addClickedMessage()
    )
    document.body.appendChild(button)

    appendPar(document.body, "Hello World")
  }

  def main(args: Array[String]): Unit = {
    val app: HtmlElement = div(
      h1("Hello Vite!")
    )

    renderOnDomContentLoaded(dom.document.querySelector("#app"), app)
  }

//  def main(args: Array[String]): Unit = {
//        document.addEventListener("DOMContentLoaded", { (e: dom.Event) =>
//            setupUI()
//        })
//  }

}
