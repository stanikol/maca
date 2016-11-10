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

  def doShopping(orderInfo: Var[OrderInfo], id: Int = 0, qnt: Int = 1) = {
    assert(qnt>0, "Quantinty must be positive!")
    val url = if(id!=0) s"/shop/$id/$qnt" else "/shop"
    println(s"Requesting $url")
    Ajax.get(url).onSuccess { case xhr =>
      val result = read[OrderInfo](xhr.responseText)
      println(s"Got respond from $url: $result")
      orderInfo := result
    }
  }

  @dom
  def order(orderInfo: Var[OrderInfo]) = {
    doShopping(orderInfo)
    val items = Vars(orderInfo.bind.items:_*)
    <div>{
      "Ваш заказ"
      for(i<-items)
        yield
          <div class="row">
            <div>{i.title}</div>
            <div>{i.toString}</div>
            <div>{Order.shopButtons(i, orderInfo, false).bind}</div>
          </div>

    }</div>
  }

//  @dom
//  private def displayOrder(items: OrderInfo) =  {
//    Constants(items.items:_*).map { i =>
//          <div class="orderItem">
//            <p>{i.toString}</p>
//            <p>{i.title}</p>
//          </div>
//        }
//  }

  @dom
  def shopButtons(i: GoodsItem, data: Var[OrderInfo], hidden: Boolean=true) = {
    val q = Var(1)
    def addToCart = { e: Event => Order.doShopping(data, i.id, q.get) }
    <div class={s"shopButtons row ${if(hidden) "hide"}"}>
      <div class="btn-floating red lighten-1">
        <i class="fa fa-minus-square-o"
           data:aria-hidden="true"
           onclick={ event: Event =>
             val newQnt = q.get - 1
             q := (if(newQnt>0) newQnt else 1)
           }
        ></i>
      </div>
      <div class={s"center-block center btn-flat waves-effect waves-red ${if(hidden) "disabled"}"} onclick={addToCart}>
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
    Order.doShopping(shopped)
    <a class="shopingCart" href="#order">
      <i class="material-icons medium">shopping_cart</i>
      <span>
        { shopped.bind.qnt.toString } ед. на сумму <br/>{ shopped.bind.total.toString } грн.
      </span>
    </a>
  }
}
