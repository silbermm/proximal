package controllers

import play.api._
import play.api.mvc._
import services._
import securesocial.core._
import models._
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class StandardWithEducationLevel(standard: Standard, levels: List[EducationLevel])
case class StatementWithEducationLevel(statement: Statement, levels: List[EducationLevel])

class StandardsController(override implicit val env: RuntimeEnvironment[SecureUser]) extends securesocial.core.SecureSocial[SecureUser]{
 
  implicit val standardsFormat = Json.format[Standard]
  implicit val educationLevelFormat = Json.format[EducationLevel] 
  implicit val statementFormat = Json.format[Statement]
  implicit val standardsWithEducationLevel = Json.format[StandardWithEducationLevel]
  implicit val statementWithEducationLevel = Json.format[StatementWithEducationLevel]

  val standardsService: StandardsServiceTrait = new StandardsService()
  val educationLevelsService: EducationLevelsServiceTrait = new EducationLevelsService()

  def create = SecuredAction(BodyParsers.parse.json){ implicit request =>
    request.body.validate[StandardWithEducationLevel].fold(
      errors => { BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(errors))) },
      standard => {   
        standardsService.create(standard.standard, standard.levels).map( st => 
          Ok(Json.obj("status" -> "OK", "standard" -> Json.toJson(st)))
        ).getOrElse(
          BadRequest(Json.obj("status" -> "KO", "message" -> "Unable to add the standard"))
        )
      }
    ) 
  }

  def view(id: Long) = Action { request =>
    standardsService.findWithEducationLevels(id) match {
      case (Some(standard),levels:List[EducationLevel]) => Ok(Json.obj("standard" -> Json.toJson(standard), "levels" -> Json.toJson(levels)))
      case (None,List(_*)) => NotFound(Json.obj("status" -> "KO", "message" -> "standard not found"))
    }
  }

  def list = Action{ request =>
    Ok(Json.toJson(standardsService.list)) 
  }

  def update(id: Long) = SecuredAction(BodyParsers.parse.json){implicit request =>
    request.body.validate[Standard].fold(
      errors => { BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(errors))) },
      standard => {
        standardsService.update(id, standard) match {
          case 1 => {
            standardsService.find(id).map( st =>
                Ok(Json.obj("status" -> "OK", "standard" -> Json.toJson(st)))
              ).getOrElse(
                BadRequest(Json.obj("status" -> "KO", "message" -> "Update was successful, unable to return the object"))
              )
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

  def getStatementsForStandard(id: Long) = Action { request=>
    standardsService.findWithStatements(id) match {
      case (Some(stan), statementList) => {
        Ok(Json.obj("standard" -> Json.toJson(stan), "statements" -> Json.toJson(statementList))) 
      }
      case _ => BadRequest(Json.obj("status" -> "KO", "message" -> "there is no record with that id"))

    }
  }

  def createStatement(id: Long) = SecuredAction(BodyParsers.parse.json){ implicit request =>
    request.body.validate[StatementWithEducationLevel].fold(
      errors => { BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(errors))) },
      json => {   
        standardsService.create(json.statement.copy(standardId=Some(id)), json.levels) match {
          case (Some(statement), edLevels) => Ok(Json.obj("statement" -> Json.toJson(statement), "levels" -> Json.toJson(edLevels)))
          case _ => BadRequest(Json.obj("status" -> "KO", "message" -> "Unable to add the statement"))
        } 
      }
    ) 
  }

  def updateStatement(id: Long) = SecuredAction(BodyParsers.parse.json){ implicit request =>
    ???
  }

  def deleteStatement(id: Long, statementId: Long) = SecuredAction{ implicit request =>
    ???
  }

  def importData = SecuredAction{ implicit request =>
   ??? 
  }

}
