package controllers

import play.api._
import play.api.mvc._
import services.StandardsService
import securesocial.core._
import models._
import play.api.libs.json._
import play.api.libs.functional.syntax._

class StandardsController(override implicit val env: RuntimeEnvironment[SecureUser]) extends securesocial.core.SecureSocial[SecureUser]{
 
  implicit val standardsFormat = Json.format[Standard]

  val standardsService = new StandardsService()


  def create = SecuredAction(BodyParsers.parse.json){ implicit request =>
    request.body.validate[Standard].fold(
      errors => { BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(errors))) },
      standard => {
        standardsService.create(standard) match {
          case s => Ok(Json.obj("status" -> "OK", "standard" -> Json.toJson(s)))
          case _ => BadRequest(Json.obj("status" -> "KO", "message" -> "Unable to add the standard"))
        }
      }
    ) 
  }
  
  def list = SecuredAction{ implicit request => 
    Ok(Json.toJson(standardsService.list)) 
  }

  def update(id: Long) = SecuredAction(BodyParsers.parse.json){implicit request =>
    request.body.validate[Standard].fold(
      errors => { BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(errors))) },
      standard => {
        standardsService.update(id, standard) match {
          case 1 => {
            standardsService.find(id) match {
              case Some(s) => Ok(Json.obj("status" -> "OK", "standard" -> Json.toJson(s)))
              case None => BadRequest(Json.obj("status" -> "KO", "message" -> "Update was successful, unable to return the object"))
            }
          }
          case _ => BadRequest(Json.obj("status" -> "KO", "message" -> "unable to update the recored at this time"))
        }
      }
    )
  }

  def delete(id: Long) = SecuredAction{implicit request =>
    standardsService.find(id) match {
      case Some(standard) => {
        //TODO: make sure the user has privledges to delete this record!!!
        val deleted : Int = standardsService.delete(standard)
        if(deleted < 1){
          Ok(Json.obj("status" -> "OK", "message" -> "successfully deleted standard"))
        } else {
          BadRequest(Json.obj("status" -> "KO", "message" -> "unable to delete standard"))
        }
      }
      case None => BadRequest(Json.obj("status" -> "KO", "message" -> "there is no record with that id"))
    } 
  }

  def importData = SecuredAction{ implicit request =>
   ??? 
  }

}
