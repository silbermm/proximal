package controllers

import play.api._
import play.api.mvc._
import services._
import securesocial.core._
import models._
import helpers.RolesHelper
import play.api.Logger
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.db.slick.DB
import play.api.Play.current
import helpers.ImplicitJsonFormat._

import play.api.libs.concurrent.Akka
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration._
import scala.concurrent.Future

class QuestionsController(override implicit val env: RuntimeEnvironment[SecureUser]) extends securesocial.core.SecureSocial[SecureUser] {

  val questionActor = Akka.system.actorOf(Props[QuestionActor])
  implicit val timeout = Timeout(30 seconds)

  def create = SecuredAction.async(BodyParsers.parse.json) { implicit request =>
    request.body.validate[JsonQuestion].fold(
      errors => Future { BadRequest(Json.obj("message" -> JsError.toFlatJson(errors))) },
      question => createQuestion(question, request.user.uid.get)
    )
  }

  def update = SecuredAction(BodyParsers.parse.json) { implicit request =>
    DB.withSession { implicit s =>
      RolesHelper.admin(request.user.uid.get, uid => {
        request.body.validate[JsonQuestion].fold(
          errors => BadRequest(Json.obj("message" -> JsError.toFlatJson(errors))),
          question => {
            Questions.update(question) match {
              case 1 => Ok(Json.toJson(question))
              case 0 => NoContent
              case _ => BadRequest
            }
          }
        )
      })
    }
  }

  def list = Action { implicit request =>
    DB.withSession { implicit s =>
      val q = Questions.allWithResource
      Ok(Json.toJson(q))
    }
  }

  def get(id: Long) = Action { implicit request =>
    DB.withSession { implicit s =>
      val q = Questions.findWithStatements(id)
      Ok(Json.toJson(q))
    }
  }

  def getByResource(id: Long) = SecuredAction { implicit request =>
    Logger.debug("looking for question with resourceId: " + id);
    DB.withSession { implicit s =>
      val q = Questions.findByResourceId(id)
      Ok(Json.toJson(q));
    }
  }

  def delete(id: Long) = SecuredAction { implicit request =>
    try {
      DB.withSession { implicit s =>
        RolesHelper.admin(request.user.uid.get, uid => {
          Questions.delete(id) match {
            case 1 => Ok
            case _ => BadRequest(Json.obj("message" -> "Did not delete the record"))
          }
        })
      }
    } catch {
      case e: Throwable => BadRequest(Json.obj("message" -> s"$e"))
    }
  }

  def createQuestion(q: JsonQuestion, userId: Long): Future[Result] = {
    Logger.debug(s"Question to create = $q")
    ask(questionActor, CreateQuestion(q, userId)).mapTo[Option[JsonQuestion]] map { x =>
      x match {
        case Some(ques) => Ok(Json.toJson(ques))
        case None => play.api.mvc.Results.Unauthorized
      }
    }
  }

}
