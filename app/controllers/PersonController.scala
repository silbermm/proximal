package controllers

import play.api._
import play.api.mvc._
import services.PersonService
import securesocial.core._
import models._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import org.joda.time.DateTime

case class Child(firstName: String, lastName: String, birthDate: Long)

class PersonController(override implicit val env: RuntimeEnvironment[SecureUser])  extends securesocial.core.SecureSocial[SecureUser] {

  var personService = new PersonService()

  implicit val childFormat = Json.format[Child] 
  implicit val peopleFormat = Json.format[Person]

  //implicit val peopleWrites: Writes[Person] = (
  //  (JsPath \ "id").write[Option[Long]] and
  //  (JsPath \ "firstName").write[String] and
  //  (JsPath \ "lastName").write[Option[String]] and
  //  (JsPath \ "birthDate").write[Option[DateTime]] and
  //  (JsPath \ "uid").write[Option[Long]]
  //)(unlift(Person.unapply))

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
        personService.findPersonByUid(request.user.uid.get) match {
          case Some(pr) => {
            val p = new Person(None,person.firstName,Some(person.lastName),Some(new DateTime(person.birthDate)),None)
            val c = personService.createPerson(p)
            personService.addChild(c, pr)
            Logger.debug("got a person to add " + person.firstName)
            Ok(Json.obj("status" ->"OK", "child" -> Json.toJson(c))) 
          }
          case _ => BadRequest
        } 
      }
    )
  }

  def removeChild(id: Long) = SecuredAction {implicit request =>
    personService.findChildren(request.user.uid.get) match {
      case children : List[Person] => {
        Logger.debug(children.toString())
        children.find {child => child.id.get == id } match {
          case Some(c) => { 
            personService.deletePerson(id)
            Ok(Json.toJson(c))
          }
          case None => BadRequest(Json.obj("status" -> "KO", "message" -> ("No match in the list for id " + id.toString() )))
        }
      }
      case _ => BadRequest(Json.obj("status" -> "KO", "message" -> ("Unable to find child for " + request.user.firstName + " with an id of " + id.toString())))
    }
  }

}
