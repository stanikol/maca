package shared

import scala.util.Try
//import upickle.default._

/**
  * Created by stanikol on 10/23/16.
  */
object Model {
  case class GoodsItem(id: Int, category: String, title: String, description: String,
                       qnt: Int, price: BigDecimal, show: Int, image: List[String])

  ////////////////////////////////////////////////////////////////////////////

  type Order = Map[Int, Int]

  case class OrderItemInfo(title: String, qnt: Int, price: BigDecimal)

  object OrderItemInfo {
    def apply(goodsItem: GoodsItem, qnt: Int): OrderItemInfo = {
      assert(qnt>0, "Quantity must be > 0!")
      new OrderItemInfo(goodsItem.title, qnt, goodsItem.price)
    }
  }

  case class OrderInfo(items: Seq[OrderItemInfo], total: BigDecimal, qnt: Int)
  object OrderInfo {
    def apply(items: Seq[OrderItemInfo]): OrderInfo = {
      val (priceTotal, qntTotal) =
        items.foldLeft((0,0):Tuple2[BigDecimal,Int])((a,b) => (a._1 + b.price * b.qnt, a._2 + b.qnt))
      new OrderInfo(items, priceTotal, qntTotal)
    }

    def apply(order: Order, priceList: Seq[GoodsItem]): OrderInfo = {
      assert(order.keySet.diff(priceList.map(_.id).toSet).isEmpty, "Order and price list differs!")
      assert(order.values.forall(_>0), "Negative or zero quantities in order!")
      val items: Seq[OrderItemInfo] = priceList.map(i => OrderItemInfo(i, order(i.id)))
      apply(items)
    }
    def empty = apply(Seq.empty[OrderItemInfo])
  }
  ////////////////////////////////////////////////////////////////////////////
}


