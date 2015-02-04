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
import play.api.libs.concurrent.Akka
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout 
import scala.concurrent.duration._
import scala.concurrent.Future

case class ChildWithStandard(childId: Long, standardId: Long)

class AssessmentController(override implicit val env: RuntimeEnvironment[SecureUser]) extends securesocial.core.SecureSocial[SecureUser] {
  
  implicit val childWithStandardFormat = Json.format[ChildWithStandard]

  val newAssessmentActor = Akka.system.actorOf(Props[AssessmentActor])
  implicit val timeout = Timeout(30 seconds)

  def newAssesment = SecuredAction.async(BodyParsers.parse.json) { implicit request => 
    
    val webReq: ChildWithStandard = request.body.validate[ChildWithStandard].fold(
      errors => {
        Logger.error(errors.toString())
        ChildWithStandard(-1,-1)
      },
      obj => {        
        obj
      } 
    )

    if(webReq.childId < 1 || webReq.standardId < 1){
      Future { BadRequest(Json.obj("message" -> s"Something went wrong! $webReq.childId" )) }
    } else {
      PersonService.childActionAsync(request.user.uid.get, webReq.childId, c =>  {        
        ask(newAssessmentActor, ChildAndStandard(c, webReq.standardId)).mapTo[Option[AssessmentQuestion]] map { x =>
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
