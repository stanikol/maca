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


  @dom
  def navigationBar = {
    <nav>
      <ul data:id="dropdown1" class="dropdown-content">
        <li><a href="#goods" >Витрина</a></li>
        <li><a href="#about">О нас</a></li>
        <li class="divider"></li>
        <li><a href="#Рецепты">Рецепты</a></li>
        <li><a href="#blog">Блог</a></li>
        <li class="divider"></li>
        <li><a href="#Всем мира и добра ! ;)" >Всем мира и добра ! ;)</a></li>
      </ul>
      <div class="nav-wrapper ">
        <a href="#" class="brand-logo center">Candy Bar "Макаруна"</a>
        <ul class="right hide-on-small-only">
          <li><a href="#" >Витрина</a></li>
          <li><a href="#О нас">О нас</a></li>
          <!-- Dropdown Trigger -->
          <li><a class="dropdown-button" href="#!" data:data-activates="dropdown1">
                Меню<i class="material-icons right">arrow_drop_down</i></a>
          </li>
        </ul>
      </div>

    </nav>
      <button data:data-activates="slide-out" class="button-collapse hide-on-med-and-up"><i class="material-icons">menu</i></button>
      <ul data:id="slide-out" class="side-nav">
        <li><a href="#about">О нас</a></li>
        <li class="divider"></li>
        <li><a href="#goods" >Витрина</a></li>
        <li><a href="#Рецепты">Рецепты</a></li>
        <li><a href="#blog">Блог</a></li>
        <li class="divider"></li>
        <li><a href="#" >Витрина</a></li>
        <li><a href="#Всем мира и добра ! ;)" >Всем мира и добра ! ;)</a></li>
    </ul>
  }


  window.onhashchange = { _: Event => router := js.Dynamic.global.decodeURIComponent(window.location.hash.drop(1)).toString }

  val router = Var("")

  @dom
  def content: Binding[Node] = {
    router.bind match {
      case "goods"    =>  Goods.renderGoods.bind
      case x if x.startsWith("qnt") =>
        js.Dynamic.global.alert(s"Вы только-что купили $x")
        Goods.renderGoods.bind
      case unknown=> <div>{unknown}</div>
    }
  }


  @dom
  def desktop = {
    <div>
      <!-- Dropdown Structure -->
      { navigationBar.bind }
      <div data:id = "content">
        { content.bind }
      </div>

    </div>
  }


  import js.DynamicImplicits._
  import js.Dynamic.{global => g}


  def main(): Unit = {
    val d = dom.render(document.body, desktop)
  }
}
