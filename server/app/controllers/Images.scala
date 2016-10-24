package controllers

import java.io.{BufferedInputStream, FileInputStream, FileOutputStream}
import javax.inject._

import models.Tables._
import slick.driver.PostgresDriver.api._
import play.api.mvc._
import play.api.Logger
import play.api.cache.{CacheApi, Cached}
import play.api.data._
import play.api.data.Forms._
import play.api.libs.concurrent.Execution.Implicits._
import com.sksamuel.scrimage.{Image => Img}
import net.sf.ehcache.{CacheManager, Ehcache}
import play.api.libs.json.Json

import scala.concurrent.Future
import scala.util._

class Images @Inject()(db: models.DBService,  cached: Cached, cacheMan: CacheManager) extends Controller {

  /**
    * Save and resize image file.
    * Form should submit following params:
    *   image           - a file;
    *   height & width  - new size (if omitted then no
    *                     resizing will be performed).
    * @return Action with result message
    */
  def save =  Action.async(parse.multipartFormData) { implicit request =>
    val resizeTo: Option[(Int, Int)] =
      Form(tuple("height" -> number, "width" -> number))
        .bindFromRequest().fold(
          withErrors => None,
          ok => Some(ok)
        )
    request.body.file("image").map { picture =>
      val filename = picture.filename
      val contentType = picture.contentType
      val newImage = Image(
        filename,
        contentType.getOrElse(BINARY),
        resizeTo match {
          case Some((height, width)) =>
              val uploadedImage = Img.fromFile(picture.ref.file)
              val resizedImage = uploadedImage.scaleTo(height, width)
               resizedImage.bytes
          case _ =>
              val bis = new BufferedInputStream(new FileInputStream(picture.ref.file))
              Stream.continually(bis.read).takeWhile(_ != -1).map(_.toByte).toArray
        }
      )
      val addNewImage = images += newImage
      db.runAsync(addNewImage).map { res => res
        val msg: String =
          s"\n${contentType.map(c=>s"$c with filename ").getOrElse("")} `$filename` is saved.\n" +
            resizeTo.map{ case (h, w) => s" and resized to $h x $w." }.getOrElse(".")
        Logger.info(msg)
        cacheMan.clearAll()
        Ok(msg)
      }
//        .recover {
//         case error => BadRequest("\n"+error.getMessage+"\n")
//      }
    }.getOrElse {
      val msg: String =
        s"\nFailed to save upload and save file to database! Perhaps file you selected is missing or something else bad happened.\n"
      Logger.error(msg)
      Future(BadRequest(msg))
    }
  }

  def getFile(file: String) = cached(s"img/$file") { Action.async { request =>
    db.runAsync(images.filter(_.name === file).result.headOption).map {
      case Some(Image(name, content, bytes)) =>
        Ok(bytes).as(content)
      case _ =>
        val msq = s"\nError while getting image `$file`.\n"
        Logger.error(msq)
        BadRequest(Array.empty[Byte]).as("image/jpg")
    }
  }}

  def list(q: Option[String], c: Option[String]) = Action.async { request =>
    val listQry = images.filter { img =>
      img.name.like(q.getOrElse("%")) && img.content.like(c.getOrElse("%"))
    }
    val result = db.runAsync(listQry.result).map { img =>
      val images = Json.toJson(img.toList.map(ImageInfo(_)))
      Ok(s"Images are \n $images")
    }
    result.onFailure{ case except: Exception => Logger.error(s"Failed to run ${listQry.result.statements.mkString("\n")}") }
    result
  }

  def delete(n: String) = Action.async { request =>
    val deleteQry = images.filter(img => img.name.like(n.replace("*","%"))).delete //.returning(images.map(_.result))
    cacheMan.clearAll()
    db.runAsync(deleteQry).map { result =>
      val msg = s"\nThere where $result images `$n` deleted from DB.\n"
      Logger.info(msg)
      Ok(msg)
    }
  }
}
