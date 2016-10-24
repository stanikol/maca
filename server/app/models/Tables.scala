package models

import org.springframework.context.annotation.Description
import play.api.http.ContentTypes
import play.api.libs.json._
import play.api.libs.functional.syntax._
//import slick.driver.PostgresDriver.api._
import models.MacaPostgresDriver.api._
import shared.Model._

/**
  * Created by stanikol on 10/21/16.
  */


object Tables {

  case class Image(name: String, content: String, bytes: Array[Byte])
  implicit val imageJosn: Format[Image] = (
      (JsPath \ "name").format[String] and
      (JsPath \ "content").format[String] and
      (JsPath \ "bytes").format[Array[Byte]]
    )(Image.apply _ , unlift(Image.unapply(_)))

  case class ImageInfo(name: String, content: String)
  object ImageInfo {

    def apply(image: Image): ImageInfo = new ImageInfo(image.name, image.content)

    implicit val imageInfoJson: Format[ImageInfo] = (
        (JsPath \ "name").format[String] and
        (JsPath \ "content").format[String]
      )(ImageInfo.apply _ , unlift(ImageInfo.unapply(_)))
  }

  class Images(tag: Tag) extends Table[Image](tag, "images") {
    def name    = column[String]("name", O.PrimaryKey)
    def content = column[String]("content")
    def bytes   = column[Array[Byte]]("bytes")
    def *       = (name, content, bytes) <> (Image.tupled, Image.unapply)
    // A reified foreign key relation that can be navigated to create a join
  }

  val images = TableQuery[Images]

  case class Category(name: String)

  class Categories(tag: Tag) extends Table[Category](tag, "categories") {
    def name = column[String]("name", O.PrimaryKey)
    def * = (name) <> (Category.apply, Category.unapply)
  }

  val categories = TableQuery[Categories]



  implicit val goodsItemJson: Format[GoodsItem] = (
      (JsPath \ "id")         .format[Int] and
      (JsPath \ "category")   .format[String] and
      (JsPath \ "title")      .format[String] and
      (JsPath \ "description").format[String] and
      (JsPath \ "qnt")        .format[Int] and
      (JsPath \ "price")      .format[BigDecimal] and
      (JsPath \ "show")       .format[Int] and
      (JsPath \ "image")      .format[List[String]]
    )(GoodsItem.apply _, unlift(GoodsItem.unapply))

  class Goods(tag: Tag) extends Table[GoodsItem](tag, "goods") {
    def id          = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def category    = column[String]      ("category")
    def title       = column[String]      ("title")
    def description = column[String]      ("description")
    def qnt         = column[Int]         ("qnt",   O.Default(0))
    def price       = column[BigDecimal]  ("price", O.Default(0))
    def show        = column[Int]         ("show",  O.Default(0))
    def image       = column[List[String]]("image", O.Default(List.empty[String]))
    def * = (id, category, title, description, qnt, price, show, image) <>
              (GoodsItem.tupled, GoodsItem.unapply)
  }

  val goods = TableQuery[Goods]




}

