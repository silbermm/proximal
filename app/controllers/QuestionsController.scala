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

  val personService = new PersonService()
  
  def create = SecuredAction(BodyParsers.parse.json) { implicit request =>
    DB.withSession{ implicit s =>
      Logger.debug(request.user.toString)
      if ( People.isAdminByUid(request.user.uid.get)) {
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

  def update(id: Long) = SecuredAction(BodyParsers.parse.json) { implicit request =>
    DB.withSession{ implicit s=> 
      if ( People.isAdminByUid(request.user.uid.get)) {
        Ok;
      }
      Unauthorized;
    }
  }

  def list = Action {implicit request =>
    DB.withSession{ implicit s=>
      val q = Questions.allWithStatements
      Ok(Json.toJson(q))
    }
  } 
  
  def get(id: Long) = Action{ implicit request => 
    DB.withSession{ implicit s=> 
      val q = Questions.findWithStatements(id) 
      Ok(Json.toJson(q)) 
    }
  }
}
