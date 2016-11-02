package controllers

import javax.inject._

import play.api.mvc._
import shared.SharedMessages

class MacaAdmin @Inject()(implicit webJarAssets: WebJarAssets) extends Controller {

  def index = Action {
    Ok(views.html.macaAdminIndex(SharedMessages.itWorks))
  }

}
