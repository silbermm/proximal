package controllers

import play.api._
import play.api.mvc._
import securesocial.core._
import models._
import helpers.RolesHelper
import play.api.Logger
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.db.slick.DB
import play.api.Play.current
import helpers.ImplicitJsonFormat._

class QuestionsController(override implicit val env: RuntimeEnvironment[SecureUser]) extends securesocial.core.SecureSocial[SecureUser] {

  def create = SecuredAction(BodyParsers.parse.json) { implicit request =>
    DB.withSession { implicit s =>
      RolesHelper.admin(request.user.uid.get, uid => {
        request.body.validate[JsonQuestion].fold(
          errors => BadRequest(Json.obj("message" -> JsError.toFlatJson(errors))),
          question => {
            if (question.statements.isEmpty) {
              Ok(Json.toJson(Questions.create(question)))
            } else {
              Questions.create(question, question.statements.get) match {
                case (Some(qs), ss) => Ok(Json.toJson(qs))
                case _ => BadRequest(Json.obj("message" -> "Unable to create the question"))
              }
            }
          }
        )
      })
    }
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
      val q = Questions.allWithStatements
      Ok(Json.toJson(q))
    }
  }

  def get(id: Long) = Action { implicit request =>
    DB.withSession { implicit s =>
      val q = Questions.findWithStatements(id)
      Ok(Json.toJson(q))
    }
  }

  def delete(id: Long) = SecuredAction { implicit request =>
    DB.withSession { implicit s =>
      RolesHelper.admin(request.user.uid.get, uid => {
        Questions.delete(id) match {
          case 1 => Ok
          case _ => BadRequest(Json.obj("message" -> "Did not delete the record"))
        }
      })
    }
  }
}
