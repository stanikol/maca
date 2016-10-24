package example

import com.thoughtworks.binding.Binding.{Var, Vars}
import com.thoughtworks.binding.dom
import org.scalajs.dom._
import org.scalajs.dom.ext.Ajax
import org.scalajs.jquery._
import shared.Model.GoodsItem
import upickle.default._

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js

/**
  * Created by stanikol on 10/24/16.
  */
object Goods {
  def listGoods(data: Vars[GoodsItem]) = {
    val url = "/goods"
    Ajax.get(url).onSuccess { case xhr =>
      data.get.clear()
      data.get ++= read[Seq[GoodsItem]](xhr.responseText)
    }
  }

  @dom
  def qnt(i: GoodsItem) = {
    val q = Var("1")
    def addToCart = { e: Event =>
      js.Dynamic.global.alert(s"You bought ${i.title} * ${q.get} ${i.price}!")
    }
    <div class="qnt hide">
      <div class="btn-floating red lighten-1">
      <i class=" material-icons small fa fa-minus-square-o"
         data:aria-hidden="true"
         onclick={ event: Event =>
           val newQnt = q.get.toInt - 1
           q := (if(newQnt>0) newQnt else 1).toString
         }
      />
      </div>
      <div class="center-block center btn-flat waves-effect waves-red" onclick={addToCart}>
        <i class="fa fa-cart-arrow-down center-block center" data:aria-hidden="true">{q.bind} шт.</i>
      </div>
      <div class="btn-floating red lighten-1">
        <i class="material-icons small fa fa-plus-square-o"
           data:aria-hidden="true"
           onclick={event: Event =>
             q := (q.get.toInt + 1).toString
           }
        />
      </div>
    </div>
  }

  @dom
  def renderGoods = {
    val showroom = Vars.empty[GoodsItem]
    listGoods(showroom)
    // Factory for event handler to show/hide buy button
    def onMouseOverOut(i: GoodsItem) = {
      def eventHandler(e: Event) = {
        jQuery(s"#qnt${i.id} .qnt").toggleClass("hide")
        jQuery(s"#qnt${i.id} .price").toggleClass("hide")
      }
      eventHandler _
    }
    val resultDom =
      <div class="row">
        {for (i <- showroom) yield {
          <div class="col s3" onmouseover={ onMouseOverOut(i) } onmouseout= { onMouseOverOut(i) }>
              <div class="card small" data:id={s"qnt${i.id}"}>
                <div class="card-image waves-effect waves-block waves-light">
                  <img class="activator" src={i.image.headOption.getOrElse("")}/>
                  </div>
                  <div class="card-content">
                    <span class="card-title activator grey-text text-darken-4 float-text">{i.title}
                      <i class="material-icons right">more_vert</i>
                    </span>
                    <p class="price">{i.price.toString } грн.</p>
                    { qnt(i).bind }
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

}
