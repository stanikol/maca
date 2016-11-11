/**
  * Created by stanikol on 11/5/16.
  */
import com.thoughtworks.binding.Binding.{BindingSeq, Constants, Var, Vars}
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom._
import org.scalajs.dom.ext.Ajax
import org.scalajs.jquery._
import shared.Model._
import upickle.default._

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.xml.NodeBuffer

/**
  * Created by stanikol on 10/24/16.
  */
object Order {

  def callShopAPI(orderInfo: Var[OrderInfo], id: Int = 0, qnt: Int = 1) = {
    assert(qnt>0, "Quantinty must be positive!")
    val url = if(id!=0) s"/shop/$id/$qnt" else "/shop"
//    println(s"Requesting $url")
    Ajax.get(url).onSuccess { case xhr =>
      val result = read[OrderInfo](xhr.responseText)
      println(s"Got respond from $url: $result")
      orderInfo := result
    }
  }

//  @dom
//  def order(orderInfo: Var[OrderInfo]) =
//  {
//    <div class="row">
//      <div class="col s8">{orderedItems(orderInfo).bind}</div>
//    </div>
//  }

//  @dom
//  def order(orderInfo: Var[OrderInfo]) = orderedItems(orderInfo).bind

  @dom
//  def orderedItems(orderInfo: Var[OrderInfo]) = {
  def order(orderInfo: Var[OrderInfo]) = {
    callShopAPI(orderInfo)
    val items = Vars(orderInfo.bind.items:_*)
    <div class="orderedItems">
      {
      "Ваш заказ"
      for(i<-items)
        yield
          <div class="orderRow"
               data:id={s"shop-${i.id}"}
               onmouseover={ Order.toggleShopButtonsHandlerFactory(i) }
               onmouseout= { Order.toggleShopButtonsHandlerFactory(i) } >
            <img class="pic responsive-img"
                 src={i.image.headOption.getOrElse("")}
            />
            <div class="title">{i.title}</div>
            <div class="qnt-price">{i.qnt.toString} шт. * &#x20b4; {i.price.toString}</div>
            <div class="total">&#x20b4; {(i.price * i.qnt) toString}</div>
            <div class="shoppingButtons">{Order.shopButtons(i, orderInfo, i.qnt).bind}</div>
          </div>
      }
      <div class="divider"></div>
      <div class="totalText">Итого сумма заказа:</div>
      <div class="totalAmount">&#x20b4; {items.get.foldLeft(0:BigDecimal){(a,n)=>a + n.price*n.qnt}.toString}</div>
    </div>
  }

  // Factory for event handler to show/hide buy button
  def toggleShopButtonsHandlerFactory(i: GoodsItem) = {
    def eventHandler(e: Event) = {
//      println(s"toggle $i")
      jQuery(s"#shop-${i.id} .shopButtons").toggleClass("hide")
      jQuery(s"#shop-${i.id} .price").toggleClass("hide")
      jQuery(s"#shop-${i.id} .btn-flat").toggleClass("disabled")
    }
    eventHandler _
  }

  @dom
  def shopButtons(i: GoodsItem, data: Var[OrderInfo], qnt: Int = 1) = {
    val q = Var(qnt)
    def addToCart = { e: Event => Order.callShopAPI(data, i.id, q.get) }
    <div class="shopButtons row hide">
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

  @dom
  def shopingCart(shopped:Var[OrderInfo]) = {
    Order.callShopAPI(shopped)
    <a class="shopingCart" href="#order">
      <i class="material-icons medium">shopping_cart</i>
      <span>
        { shopped.bind.qnt.toString } ед. на сумму <br/>{ shopped.bind.total.toString } грн.
      </span>
    </a>
  }
}
