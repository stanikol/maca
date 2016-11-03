import com.thoughtworks.binding.Binding.Var
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.Node
import org.scalajs.dom.{Event, document, window}

import scala.scalajs.js

//import io.udash.wrappers.jquery._
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
      case ""         =>  Goods.goods(shopped).bind
      case x if x.startsWith("qnt") =>
        js.Dynamic.global.alert(s"Вы только-что купили $x")
        Goods.goods(shopped).bind
      case unknown=> <div>{unknown}</div>
    }
  }

  @dom
  private def shopingCart = {
    Goods.doShopping(shopped)
    <div class="col s12 m5 l3 right container row">
      <div class="col s2 m3">
        <i class="material-icons medium">shopping_cart</i>
      </div>
      <div class="col s10 m9 flow-text">
        { shopped.bind.qnt.toString } ед. на сумму { shopped.bind.total.toString } грн.<br/>
        <a href="#payment" class="btn btn-default small">
          Оформить
        </a>
      </div>
    </div>
  }

  @dom
  def macaWebMain: Binding[Node] = {
    <div>
      { NavigationBar.navigationBar.bind }
      <div data:id = "content">
        <div class="row">
            { shopingCart.bind }
        </div>
        { content.bind }<br/>
      </div>

    </div>
  }

  def main(): Unit = {
    val d = dom.render(document.body, macaWebMain)
  }
}
