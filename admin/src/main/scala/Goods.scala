package example

import com.sun.xml.internal.org.jvnet.staxex.NamespaceContextEx.Binding
import com.thoughtworks.binding.Binding.{Var, Vars}
import com.thoughtworks.binding.dom
import org.scalajs.dom._
import org.scalajs.dom.ext.Ajax
import org.scalajs.jquery._
import shared.Model._
import upickle.default._

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js

/**
  * Created by stanikol on 10/24/16.
  */
object Goods {
  def getGoodsFromServer(data: Vars[GoodsItem]) = {
    val url = "/goods"
    Ajax.get(url).onSuccess { case xhr =>
      data.get.clear()
      data.get ++= read[Seq[GoodsItem]](xhr.responseText)
    }
  }

  def doShopping(data: Var[OrderInfo], id: Int = 0, qnt: Int = 1) = {
    assert(qnt>0, "Quantinty must be positive!")
    val url = if(id!=0) s"/shop/$id/$qnt" else "/shop"
    println(url)
    Ajax.get(url).onSuccess { case xhr =>
      data := read[OrderInfo](xhr.responseText)
    }
  }

  @dom
  def goods(shopCard: Var[OrderInfo]) = {
    val listOfGoods = Vars.empty[GoodsItem]
    getGoodsFromServer(listOfGoods)

    // Factory for event handler to show/hide buy button
    def onMouseOverOut(i: GoodsItem) = {
      def eventHandler(e: Event) = {
        jQuery(s"#shop-${i.id} .shop").toggleClass("hide")
        jQuery(s"#shop-${i.id} .price").toggleClass("hide")
        jQuery(s"#shop-${i.id} .btn-flat").toggleClass("disabled")
      }
      eventHandler _
    }
    val resultDom =
    <div class="row">
      {for (i <- listOfGoods) yield {
      <div class="col m3 s12" onmouseover={ onMouseOverOut(i) } onmouseout= { onMouseOverOut(i) }>
        <div class="card small" data:id={s"shop-${i.id}"}>
          <div class="card-image waves-effect waves-block waves-light">
          <img class="activator responsive-img" src={i.image.headOption.getOrElse("")}/>
          </div>
          <div class="card-content">
            <span class="card-title activator grey-text text-darken-4 float-text">{i.title}
              <i class="material-icons right">more_vert</i>
            </span>
            <p class="price">{i.price.toString } грн.</p>
            { shopButtons(i, shopCard).bind }
          </div>
          <div class="card-reveal">
            <span class="card-title grey-text text-darken-4">{i.title}<i class="material-icons right">close</i></span>
            <p>{i.description}  </p>
          </div>
        </div>
      </div>
    }}
    </div>
    resultDom
  }

  @dom
  private def shopButtons(i: GoodsItem, data: Var[OrderInfo]) = {
    val q = Var(1)
    def addToCart = { e: Event => doShopping(data, i.id, q.get) }
    <div class="shop hide">
      <div class="btn-floating red lighten-1">
        <i class="fa fa-minus-square-o"
           data:aria-hidden="true"
           onclick={ event: Event =>
             val newQnt = q.get - 1
             q := (if(newQnt>0) newQnt else 1)
           }
        ></i>
      </div>
      <div class="center-block center btn-flat waves-effect waves-red disabled" onclick={addToCart}>
        <i class="fa fa-cart-arrow-down center-block center" data:aria-hidden="true"></i>{q.bind.toString} шт.
      </div>
      <div class="btn-floating red lighten-1">
        <i class="fa fa-plus-square-o"
           data:aria-hidden="true"
           onclick={ event: Event => q := q.get + 1 }
        ></i>
      </div>
    </div>
  }
}
