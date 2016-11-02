package example

import com.thoughtworks.binding.Binding.{Constants, Var, Vars}
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.{Event, document, window}
import org.scalajs.dom.raw.{HTMLInputElement, Node, HTMLDivElement}
import org.scalajs.dom.ext.Ajax

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import org.scalajs.jquery.jQuery

//import io.udash.wrappers.jquery._
import upickle.default._
import shared.Model._

object MacaWebMain extends js.JSApp {

  window.onhashchange = { _: Event =>
    router := js.Dynamic.global.decodeURIComponent(window.location.hash.drop(1)).toString
  }

  private val router = Var("goods")
  private val shopped: Var[OrderInfo] = Var(OrderInfo.empty)

  @dom
  private def content: Binding[Node] = {
    router.bind match {
      case "goods"    =>  Goods.goods(shopped).bind
      case x if x.startsWith("qnt") =>
        js.Dynamic.global.alert(s"Вы только-что купили $x")
        Goods.goods(shopped).bind
      case unknown=> <div>{unknown}</div>
    }
  }

  @dom
  def macaWebMain = {
    Goods.doShopping(shopped)
    <div>
      { NavigationBar.navigationBar.bind }
      <div data:id = "content">
        { shopped.bind.toString }<br/>
        { content.bind }<br/>
      </div>

    </div>
  }


  import js.DynamicImplicits._
  import js.Dynamic.{global => g}


  def main(): Unit = {
    val d = dom.render(document.body, macaWebMain)
  }
}
