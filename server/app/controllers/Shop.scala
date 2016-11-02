package controllers

import javax.inject._

import com.sksamuel.scrimage.{Image => Img}
import models.Tables._
import play.api.cache.Cached
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc._
import slick.driver.PostgresDriver.api._
import upickle.default._
import shared.Model._

import scala.concurrent.Future
import scala.util.Try

class Shop @Inject()(db: models.DBService, cached: Cached) extends Controller {

  private val sessionKey = "goods"

  def index(_id: String = "0", _qnt: String = "1") = Action.async { request =>
    val id  = Try(_id.toInt).getOrElse(0)
    val qnt = Try(_qnt.toInt).getOrElse(1)
    assert(qnt > 0, "Negative good`s quantity is given!")
    val currentOrder: Order =
      Try(read[Order](request.session.get(sessionKey).get)).getOrElse(Map.empty)
    val updatedOrder: Order =
      if(id == 0) {
        currentOrder
      } else if(currentOrder.keySet.contains(id)) {
        currentOrder.updated(id, currentOrder(id) + qnt)
      } else {
        currentOrder + (id->qnt)
      }
    val queryPriceList = goods.filter{ row =>
      row.id inSet(updatedOrder.keys)
    }.result
    db.runAsync(queryPriceList).map { priceList: Seq[GoodsItem] =>
      val orderInfo = OrderInfo(updatedOrder, priceList)
      Ok(write(orderInfo))
        .withSession(request.session + (sessionKey -> write[Order](updatedOrder)))
    }
  }


}
