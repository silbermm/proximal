package controllers


import org.apache.commons.codec.binary.Base64
import play.api._
import play.api.mvc._
import services._
import securesocial.core._
import models._
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class JsonQuestion(id: Option[Long], text: String, picture: Option[String], typeId: Option[Long])

class QuestionsController(override implicit val env: RuntimeEnvironment[SecureUser]) extends securesocial.core.SecureSocial[SecureUser]{

  implicit val questionFormat = Json.format[JsonQuestion]

  // initalize the services
  // TODO: make use of dependency injection for this
  val personService = new PersonService()
  val questionsService : QuestionsService = new QuestionsDBService()


  def create = SecuredAction(BodyParsers.parse.json) { implicit request =>  
    if (personService.isAdmin(request.user.uid.get)) { 
      request.body.validate[JsonQuestion].fold(
        errors => BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(errors))),
        question => {
          val ques = convertToQuestion(question)
          questionsService.create(ques).map(q =>
              Ok(Json.toJson(convertToJsonQuestion(q)))
          ).getOrElse(
            BadRequest(Json.obj("message" -> "Unable to create the question"))
          )
        }
      )
    } else {
      Unauthorized(Json.obj("message" -> "You do not have permission to perform that action"))
    }
  }

  def list = SecuredAction {implicit request =>
    val questions = for { 
      q <- questionsService.all
    } yield convertToJsonQuestion(q) 
    Ok(Json.toJson(questions))
  } 

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
    JsonQuestion(q.id,q.text,p,q.typeId)
  }

}
