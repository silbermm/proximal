package controllers

import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout
import helpers.ImplicitJsonFormat._
import models._
import play.api.Play.current
import play.api.libs.concurrent.Akka
import play.api.libs.json._
import play.api.mvc._
import services._
import securesocial.core._

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps

import scala.util.{ Success, Failure }

class AttemptController(override implicit val env: RuntimeEnvironment[SecureUser]) extends securesocial.core.SecureSocial[SecureUser] {

  val attemptActor = Akka.system.actorOf(Props[AttemptActor])
  implicit val timeout = Timeout(30 seconds)

  def createAttempt = SecuredAction.async(BodyParsers.parse.json) { implicit request =>
    request.body.validate[Attempt].fold(
      errors => Future { BadRequest(Json.obj("message" -> s"Something went wrong: ${JsError.toFlatJson(errors)}")) },
      obj => {
        ask(attemptActor, CreateAttempt(obj)).mapTo[Attempt] map { response =>
          Ok(Json.toJson(response))
        }
      }
    )
  }

}
