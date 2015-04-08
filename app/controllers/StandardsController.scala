package controllers

import java.util.Date

import models._
import play.api._
import play.api.libs.json._
import play.api.mvc._
import services._
import securesocial.core._

import scala.language.higherKinds

case class StandardWithEducationLevel(standard: Standard, levels: List[EducationLevel])
case class StatementWithEducationLevel(statement: Statement, levels: List[EducationLevel])
case class TupleWithList(statement: Option[Statement], levels: List[EducationLevel])

case class JsonStandard(
  id: Option[Long],
  title: Option[String],
  description: Option[String],
  subject: Option[String],
  levels: Option[List[EducationLevel]])

class StandardsController(override implicit val env: RuntimeEnvironment[SecureUser]) extends securesocial.core.SecureSocial[SecureUser] {

  implicit val standardsFormat = Json.format[Standard]
  implicit val educationLevelFormat = Json.format[EducationLevel]
  implicit val jsonStandardsFormat = Json.format[JsonStandard]
  implicit val statementFormat = Json.format[Statement]
  implicit val standardsWithEducationLevel = Json.format[StandardWithEducationLevel]
  implicit val statementWithEducationLevel = Json.format[StatementWithEducationLevel]

  implicit def tuple2Writes[Option[Statement], List[EducationLevel]](implicit aWrites: Writes[Option[Statement]], bWrites: Writes[List[EducationLevel]]): Writes[Tuple2[Option[Statement], List[EducationLevel]]] = {
    new Writes[Tuple2[Option[Statement], List[EducationLevel]]] {
      def writes(tuple: Tuple2[Option[Statement], List[EducationLevel]]) = Json.obj(
        "statement" -> aWrites.writes(tuple._1),
        "levels" -> bWrites.writes(tuple._2)
      )
    }
  }

  val standardsService: StandardsServiceTrait = new StandardsService()
  val educationLevelsService: EducationLevelsServiceTrait = new EducationLevelsService()
  val personService = new PersonService()

  def create = SecuredAction(BodyParsers.parse.json) { implicit request =>
    if (personService.isAdmin(request.user.uid.get)) {
      request.body.validate[StandardWithEducationLevel].fold(
        errors => { BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(errors))) },
        standard => {
          standardsService.create(standard.standard, standard.levels).map(st =>
            Ok(Json.obj("status" -> "OK", "standard" -> Json.toJson(st)))
          ).getOrElse(
            BadRequest(Json.obj("status" -> "KO", "message" -> "Unable to add the standard"))
          )
        }
      )
    } else {
      Unauthorized
    }
  }

  def createV2 = SecuredAction(BodyParsers.parse.json) { implicit request =>
    if (personService.isAdmin(request.user.uid.get)) {
      request.body.validate[JsonStandard].fold(
        errors => { BadRequest(JsError.toFlatJson(errors)) },
        standard => {
          val s = convertFromJsonStandard(standard)
          standardsService.create(s._1, s._2).map(st =>
            Ok(Json.toJson(st))
          ).getOrElse(
            BadRequest(Json.obj("message" -> "Unable to add the standard"))
          )
        }
      )
    } else {
      Unauthorized
    }
  }

  def view(id: Long) = Action { request =>
    standardsService.findWithEducationLevels(id) match {
      case (Some(standard), levels: List[EducationLevel]) => Ok(Json.obj("standard" -> Json.toJson(standard), "levels" -> Json.toJson(levels)))
      case (None, List(_*)) => NotFound(Json.obj("status" -> "KO", "message" -> "standard not found"))
    }
  }

  def viewV2(id: Long) = Action { request =>
    standardsService.findWithEducationLevels(id) match {
      case (Some(standard), levels: List[EducationLevel]) => {
        Ok(Json.toJson(convertToJsonStandard(standard, levels)))
      }
      case (None, List(_*)) => NotFound(Json.obj("message" -> "standard not found"))
    }
  }

  def list = Action { request =>
    Ok(Json.toJson(standardsService.list))
  }

  def update(id: Long) = SecuredAction(BodyParsers.parse.json) { implicit request =>
    request.body.validate[Standard].fold(
      errors => { BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(errors))) },
      standard => {
        standardsService.update(id, standard) match {
          case 1 => {
            standardsService.find(id).map(st =>
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

  def delete(id: Long) = SecuredAction { implicit request =>
    if (personService.isAdmin(request.user.uid.get)) {
      standardsService.find(id) match {
        case Some(standard) => {
          val deleted: Int = standardsService.delete(standard)
          Logger.debug(s"deleted: $deleted")
          if (deleted > 0) {
            Ok(Json.obj("status" -> "OK", "message" -> "successfully deleted standard"))
          } else {
            BadRequest(Json.obj("status" -> "KO", "message" -> "unable to delete standard"))
          }
        }
        case None => BadRequest(Json.obj("status" -> "KO", "message" -> "there is no record with that id"))
      }
    } else {
      Unauthorized
    }
  }

  def getStatementsForStandard(id: Long) = Action { request =>
    standardsService.findWithStatementsAndLevels(id) match {
      case (Some(stan), statementList) => {
        Ok(Json.obj("standard" -> Json.toJson(stan), "statements" -> Json.toJson(statementList)))
      }
      case _ => BadRequest(Json.obj("status" -> "KO", "message" -> "there is no record with that id"))

    }
  }

  def createStatement(id: Long) = SecuredAction(BodyParsers.parse.json) { implicit request =>
    if (personService.isAdmin(request.user.uid.get)) {
      request.body.validate[StatementWithEducationLevel].fold(
        errors => { BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(errors))) },
        json => {
          standardsService.create(json.statement.copy(standardId = Some(id)), json.levels) match {
            case (Some(statement), edLevels) => Ok(Json.obj("statement" -> Json.toJson(statement), "levels" -> Json.toJson(edLevels)))
            case _ => BadRequest(Json.obj("status" -> "KO", "message" -> "Unable to add the statement"))
          }
        }
      )
    } else {
      Unauthorized
    }
  }

  def updateStatement(id: Long) = SecuredAction(BodyParsers.parse.json) { implicit request =>
    ???
  }

  def deleteStatement(id: Long, statementId: Long) = SecuredAction { implicit request =>
    ???
  }

  def importData = SecuredAction { implicit request =>
    ???
  }

  def convertFromJsonStandard(standard: JsonStandard): (Standard, List[EducationLevel]) = {
    val stan = Standard(
      standard.id,
      standard.title.getOrElse(""),
      standard.description.getOrElse(""),
      standard.subject.getOrElse("")
    )
    val edLevels = standard.levels.getOrElse(List.empty)
    (stan, edLevels)
  }

  def convertToJsonStandard(standard: Standard, edLevels: List[EducationLevel]): JsonStandard = {
    JsonStandard(
      standard.id,
      Some(standard.title),
      Some(standard.description),
      Some(standard.subject),
      Some(edLevels)
    )
  }

}
