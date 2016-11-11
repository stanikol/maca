import com.thoughtworks.binding.Binding.{Var, Vars}
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom._
import org.scalajs.dom.ext.Ajax
import org.scalajs.jquery._
import shared.Model._
import upickle.default._

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

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

// delme!
  //  def callShopAPI(data: Var[OrderInfo], id: Int = 0, qnt: Int = 1) = {
//    assert(qnt>0, "Quantinty must be positive!")
//    val url = if(id!=0) s"/shop/$id/$qnt" else "/shop"
//    println(url)
//    Ajax.get(url).onSuccess { case xhr =>
//      data := read[OrderInfo](xhr.responseText)
//    }
//  }

  @dom
  def goods(orderInfo: Var[OrderInfo]) = {
    val listOfGoods = Vars.empty[GoodsItem]
    getGoodsFromServer(listOfGoods)

//    // Factory for event handler to show/hide buy button
//    def onMouseOverOut(i: GoodsItem) = {
//      def eventHandler(e: Event) = {
//        jQuery(s"#shop-${i.id} .shopButtons").toggleClass("hide")
//        jQuery(s"#shop-${i.id} .price").toggleClass("hide")
//        jQuery(s"#shop-${i.id} .btn-flat").toggleClass("disabled")
//      }
//      eventHandler _
//    }
    <div class="goods">
      <div class="row">
        { Order.shopingCart(orderInfo).bind }
      </div>
      <div class="row">
        {for (i <- listOfGoods) yield {
          <div class="card"
               data:id={s"shop-${i.id}"}
               onmouseover={ Order.toggleShopButtonsHandlerFactory(i) }
               onmouseout= { Order.toggleShopButtonsHandlerFactory(i) } >
            <div class="card-image">
            <img class="activator responsive-img"
                 src={i.image.headOption.getOrElse("")}/>
            </div>
            <div class="card-content">
              <span class="card-title">{i.title}
                <i class="material-icons right">more_vert</i>
              </span>
              <p class="price">{i.price.toString } грн.</p>
              { Order.shopButtons(i, orderInfo, 1).bind }
            </div>
            <div class="card-reveal">
              <span class="card-title">{i.title}<i class="material-icons right">close</i></span>
              <p>{i.description}  </p>
            </div>
          </div>
        }}
      </div>
    </div>
  }

}
