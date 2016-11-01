package controllers

import javax.inject._

import play.api.libs.json.Json
import play.api.mvc._
import shared.SharedMessages

class MacaApp @Inject()(implicit webJarAssets: WebJarAssets) extends Controller {

  def index = Action {
    Ok(views.html.macaIndex(SharedMessages.itWorks))
  }

}
