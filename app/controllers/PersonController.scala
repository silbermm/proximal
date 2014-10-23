package controllers

import play.api._
import play.api.mvc._
import services.PersonService
import securesocial.core._
import models._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import org.joda.time.DateTime

case class Child(firstName: String, lastName: String, birthDate: DateTime)

class PersonController(override implicit val env: RuntimeEnvironment[SecureUser])  extends securesocial.core.SecureSocial[SecureUser] {

  var personService = new PersonService()

  implicit val peopleReads: Reads[Child] = (
    (JsPath \ "firstName").read[String] and
    (JsPath \ "lastName").read[String] and
    (JsPath \ "birthDate").read[DateTime]
  )(Child.apply _)

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
    val childToAdd = request.body.validate[Child] 
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
