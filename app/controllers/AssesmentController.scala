package controllers

import services._
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

class AssesmentController(override implicit val env: RuntimeEnvironment[SecureUser]) extends securesocial.core.SecureSocial[SecureUser] {

  def newAssesment(childId: Long) = SecuredAction { implicit request =>
    // Make sure the logged in user is allowed to interact with this child
    PersonService.childAction(request.user.uid.get, childId, c =>  {
      val q = AssesmentService.newAssesment(c); 
      Ok(Json.obj("assesment" -> Json.toJson(q._1), "question" -> Json.toJson(q._2)))
    })
  }

  def newScore(childId: Long, assesmentId: Long) = SecuredAction(BodyParsers.parse.json) { implicit request =>
    PersonService.childAction(request.user.uid.get, childId, c => { 
      request.body.validate[QuestionScore].fold(
        errors => BadRequest(Json.obj("message" -> JsError.toFlatJson(errors))),
        qscore => {
          val q = AssesmentService.createQuestionScore(assesmentId, qscore, childId)
          Ok(Json.obj("assesment" -> Json.toJson(q._1), "question" -> Json.toJson(q._2)))
        }
      )
    })
  }
}
