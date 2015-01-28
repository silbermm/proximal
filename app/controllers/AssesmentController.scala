package controllers

import services._
import play.api._
import play.api.mvc._
import securesocial.core._
import models._
import models.actors._
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
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.Future

class AssesmentController(override implicit val env: RuntimeEnvironment[SecureUser]) extends securesocial.core.SecureSocial[SecureUser] {
  
  val newAssessmentActor = Akka.system.actorOf(Props[AssessmentActor])
  implicit val timeout = Timeout(30 seconds)

  def newAssesment = SecuredAction.async(BodyParsers.parse.json) { implicit request => 
    
    var childId: Long = request.body.validate[Person].fold(
      errors => -1,
      child => {
        child.id match {
          case Some(i) => i
          case None => -1
        }
      } 
    )

    if(childId < 1){
      scala.concurrent.Future { BadRequest(Json.obj("message" -> "You do not have access to this student")) }
    } else {
      PersonService.childActionAsync(request.user.uid.get, childId, c =>  {
        ask(newAssessmentActor, ParentAndChild(c, request.user.uid.get)).mapTo[Option[AssessmentQuestion]] map { x =>
          x match {
            case Some(obj) => Ok(Json.toJson(obj))
            case None => BadRequest(Json.obj("message" -> "Sorry, unable to create the assessment."))
          }
        }
      })
    }

    
  }


  def newScore(assesmentId: Long) = SecuredAction(BodyParsers.parse.json) { implicit request =>
    // Make sure assessment exists and get the childId that belongs to this assessment
    val childId = AssesmentService.findChildForAssessment(assesmentId);
    if(childId < 1) {
      BadRequest(Json.obj("message" -> s"assessment not found"))
    } else {
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
}
