import com.thoughtworks.binding.Binding.{Var, Vars}
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.Node
import org.scalajs.dom.{Event, UIEvent, document, window}

import scala.scalajs.js
//import scala.scalajs.js

import io.udash.wrappers.jquery._
import shared.Model._

object MacaWebMain extends js.JSApp {

  window.onhashchange = { _: Event =>
    router := js.Dynamic.global.decodeURIComponent(window.location.hash.drop(1)).toString
  }

  private val router = Var("goods")


  @dom
  private def content: Binding[Node] = {
    router.bind match {
      case "order"    =>  Order.order(orderInfo).bind
      case "goods"    =>  Goods.goods(orderInfo).bind
      case ""         =>  Goods.goods(orderInfo).bind
      case x if x.startsWith("qnt") =>
        js.Dynamic.global.alert(s"Вы только-что купили $x")
        Goods.goods(orderInfo).bind
      case unknown=> <div> {unknown} </div>
    }
  }


  private val orderInfo: Var[OrderInfo] = Var(OrderInfo.empty)

  @dom
  def macaWebMain = {
//    val listOfGoods = Vars.empty[GoodsItem]
//    getGoodsFromServer(listOfGoods)
      <div data:id = "mainWindow">
        <div>
          { content.bind }
        </div>
      </div>
  }

  val $ = js.Dynamic.global.$
  def main(): Unit = {
    val d = dom.render(document.body.getElementsByClassName("mainWindow")(0), macaWebMain)
//    val foo = { e: UIEvent =>
//      $("ul.tabs").tabs()
//    }
//    document.onactivate = foo

  }
}
