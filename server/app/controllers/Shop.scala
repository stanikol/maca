package controllers

import javax.inject._

import com.mohiva.play.silhouette.api.{LoginEvent, Silhouette}
import com.mohiva.play.silhouette.api.exceptions.ProviderException
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.{Clock, Credentials, PasswordHasherRegistry}
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import com.mohiva.play.silhouette.impl.exceptions.IdentityNotFoundException
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import com.sksamuel.scrimage.{Image => Img}
import models.MailTokenUser
import models.Tables._
import play.api.Configuration
import play.api.cache.Cached
import play.api.i18n.{Messages, MessagesApi}
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc._
import slick.driver.PostgresDriver.api._
import upickle.default._
import shared.Model._
import utils.MailService
import utils.silhouette.{AuthController, MailTokenService, MyEnv, UserService}

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration
import scala.util.Try

class Shop @Inject()( db: models.DBService,
                      cached: Cached,
                         val silhouette: Silhouette[MyEnv],
                         val messagesApi: MessagesApi,
                         userService: UserService,
                         authInfoRepository: AuthInfoRepository,
                         credentialsProvider: CredentialsProvider,
//                         tokenService: MailTokenService[MailTokenUser],
//                         passwordHasherRegistry: PasswordHasherRegistry,
//                         mailService: MailService,
//                         conf: Configuration,
                         clock: Clock
                       ) extends AuthController {

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
        currentOrder.updated(id, qnt)
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

  def logIn = UnsecuredAction.async { implicit request =>
    //TODO:
    credentialsProvider.authenticate(Credentials("master@myweb.com", "123123")).flatMap { loginInfo =>
      userService.retrieve(loginInfo).flatMap {
        case Some(user) => for {
          authenticator <- env.authenticatorService.create(loginInfo)
          cookie <- env.authenticatorService.init(authenticator)
          result <- env.authenticatorService.embed(cookie, Ok(s"OK ${user.email}"))
        } yield {
          env.eventBus.publish(LoginEvent(user, request))
          result
        }
        case None => Future.failed(new IdentityNotFoundException("Couldn't find user"))
      }
    }.recover {
      case e: ProviderException => Redirect(routes.Auth.signIn).flashing("error" -> Messages("auth.credentials.incorrect"))
    }
  }


}
