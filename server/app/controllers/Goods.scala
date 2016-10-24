package controllers

import java.io.{BufferedInputStream, FileInputStream}
import javax.inject._

import com.sksamuel.scrimage.{Image => Img}
import models.Tables._
import play.api.Logger
import play.api.cache.Cached
import play.api.data.Forms._
import play.api.data._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc._
import play.twirl.api.Html
import slick.driver.PostgresDriver.api._

import scala.concurrent.Future

import upickle.default._

class Goods @Inject()(db: models.DBService, cached: Cached) extends Controller {

  def index = Action.async { request =>
    val query = goods.result
    db.runAsync(query).map { goods =>
      Ok(write(goods)).withSession(request.session +
        ("goods"  -> "goods") +
        ("another"-> "another")
      )
    }
  }


}
