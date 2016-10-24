package shared

/**
  * Created by stanikol on 10/23/16.
  */
object Model {
  case class GoodsItem(id: Int, category: String, title: String, description: String,
                       qnt: Int, price: BigDecimal, show: Int, image: List[String])
}
