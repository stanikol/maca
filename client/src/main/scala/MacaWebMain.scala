import com.thoughtworks.binding.Binding.{Constant, Var, Vars}
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.Node
import org.scalajs.dom.{Event, UIEvent, document, window}

import scala.scalajs.js
import scala.xml.Elem
//import scala.scalajs.js

import io.udash.wrappers.jquery._
import shared.Model._

object MacaWebMain extends js.JSApp {

  private val orderInfo: Var[OrderInfo] = Var(OrderInfo.empty)

  window.onhashchange = { _: Event => doRouting() }

  private def getCurrentRoute(): String =
    js.Dynamic.global.decodeURIComponent(window.location.hash.drop(1)).toString

  private def doRouting(): Unit = {
    val route = getCurrentRoute()
    println(s"`$route` =>>>")
    route match{
      case x if x.isEmpty =>  show(Goods.goods(orderInfo))
      case "goods"        =>  show(Goods.goods(orderInfo))
      case "order"        =>  show(Order.order(orderInfo))
      case x: String      =>  show(strToBinding(x))
    }
  }

  @dom
  private def strToBinding(s: String) = <div>{s}</div>

  @dom
  private def show(domElem: Binding[Node]) = {
    val r = dom.render(document.body.getElementsByClassName("mainWindow")(0), domElem)
  }

  def main(): Unit = {
    doRouting()
  }
}
