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

class ScoresController(override implicit val env: RuntimeEnvironment[SecureUser]) extends securesocial.core.SecureSocial[SecureUser] {

  val scoreActor = Akka.system.actorOf(Props[ScoreActor])
  implicit val timeout = Timeout(30 seconds)

  def showScores(studentId: Long) = SecuredAction.async { implicit request =>
    ask(scoreActor, ListScores(studentId)).mapTo[List[Score]] map { x =>
      Ok(Json.toJson(x))
    }
  }

  def createScore = SecuredAction.async(BodyParsers.parse.json) { implicit request =>
    request.body.validate[Score].fold(
      errors => { Future { BadRequest(Json.obj("message" -> s"Something went wrong! $errors")) } },
      obj => {
        ask(scoreActor, CreateScore(obj)).mapTo[Score] map { x =>
          Ok(Json.toJson(x))
        }
      }
    )

  }

  def updateScore = SecuredAction.async(BodyParsers.parse.json) { implicit request =>
    request.body.validate[Score].fold(
      errors => { Future { BadRequest(Json.obj("message" -> s"Something went wrong! $errors")) } },
      obj => {
        ask(scoreActor, UpdateScore(obj)).mapTo[Long] map { x =>
          if (x > 0) Ok(Json.toJson(obj)) else NoContent
        }
      }
    )
  }

}
