package controllers


import org.apache.commons.codec.binary.Base64
import play.api._
import play.api.mvc._
import services._
import securesocial.core._
import models._
import play.api.Logger
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.db.slick.DB
import play.api.Play.current
import helpers.ImplicitJsonFormat._

class QuestionsController(override implicit val env: RuntimeEnvironment[SecureUser]) extends securesocial.core.SecureSocial[SecureUser] {

  // initalize the services
  // TODO: make use of dependency injection for this
  val personService = new PersonService()
  
  def create = SecuredAction(BodyParsers.parse.json) { implicit request =>
    DB.withSession{ implicit s =>
      Logger.debug(request.user.toString)
      if (personService.isAdmin(request.user.uid.get)) { 
        request.body.validate[JsonQuestion].fold(
          errors => BadRequest(Json.obj("message" -> JsError.toFlatJson(errors))),
          question => {
            if(question.statements.isEmpty) {    
              Ok(Json.toJson(Questions.create(question)))
            } else {
              Questions.create(question, question.statements.get) match {
                case (Some(qs), ss) => Ok(Json.toJson(qs))
                case _  => BadRequest(Json.obj("message" -> "Unable to create the question"))
              }
            }
          }
        )
      } else {
        Unauthorized(Json.obj("message" -> "You do not have permission to perform that action"))
      }
    }
  }

  def list = Action {implicit request =>
    //val questions = for { 
    //  (q, l)<- QuestionsService.allWithStatements
    //} yield convertToJsonQuestion(q.get, l) 
    //Ok(Json.toJson(questions))
    Ok
  } 
  
  def get(id: Long) = Action{ implicit request => 
    //QuestionsService.findWithStatements(id) match {
    //  case (Some(question), statements) => Ok(Json.toJson(convertToJsonQuestion(question,statements)))
     // case _ => BadRequest(Json.obj("message" -> "Unable to find a question with that id"))
      Ok 
    //}
  }

/*
  def convertToQuestion(q : JsonQuestion): Question = {
    val p =  q.picture.map(pic =>
      Some(Base64.decodeBase64(pic))
    ).getOrElse(
      None
    )
    Question(q.id,q.text,p,q.typeId) 
  }

  def convertToJsonQuestion(q: Question): JsonQuestion = {
    val p = q.picture.map(pic =>
      Some(Base64.encodeBase64String(pic))
    ).getOrElse(
      None
    )
    JsonQuestion(q.id,q.text,p,q.typeId,None)
  }

  def convertToJsonQuestion(q: Question, l: List[Statement]) : JsonQuestion = {
    val jsonQuestion : JsonQuestion = convertToJsonQuestion(q)
    jsonQuestion.copy(statements = Some(l))
  }
*/
}
