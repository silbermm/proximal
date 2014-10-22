package controllers

import play.api._
import play.api.mvc._
import services.PersonService
import securesocial.core._
import models._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import org.joda.time.DateTime

class PersonController(override implicit val env: RuntimeEnvironment[SecureUser])  extends securesocial.core.SecureSocial[SecureUser] {

  var personService = new PersonService()

  implicit val peopleReads: Reads[Person] = (
    (JsPath \ "id").read[Option[Long]] and
    (JsPath \ "firstName").read[String] and
    (JsPath \ "lastName").read[Option[String]] and
    (JsPath \ "birthDate").read[Option[DateTime]] and
    (JsPath \ "uid").read[Option[Long]]
  )(Person.apply _)

  implicit val peopleWrites: Writes[Person] = (
    (JsPath \ "id").write[Option[Long]] and
    (JsPath \ "firstName").write[String] and
    (JsPath \ "lastName").write[Option[String]] and
    (JsPath \ "birthDate").write[Option[DateTime]] and
    (JsPath \ "uid").write[Option[Long]]
  )(unlift(Person.unapply))

  def children = SecuredAction{ implicit request => 
    var c = personService.findChildren(request.user.uid.get)
    var j = Json.obj("children" -> Json.toJson(c)) 
    Ok(j)
  }

  def addChild = SecuredAction(BodyParsers.parse.json){ implicit request =>
    val childToAdd = request.body.validate[Person] 
    childToAdd.fold(
      errors => {
        BadRequest(Json.obj("status" ->"KO", "message" -> JsError.toFlatJson(errors))) 
      },
      person => {
        Ok(Json.obj("status" ->"OK", "message" -> ("Child added to account") ))
      }
    )
  }

}
