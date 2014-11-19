package controllers

import play.api._
import play.api.mvc._
import services._
import securesocial.core._
import models._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import org.joda.time.DateTime

case class Child(id: Option[Long], firstName: String, lastName: String, birthDate: Long, educationLevel: Option[EducationLevel])
case class Profile(user: SecureUser, roles: List[Role])
class PersonController(override implicit val env: RuntimeEnvironment[SecureUser])  extends securesocial.core.SecureSocial[SecureUser] {

  var personService = new PersonService()
  var educationLevelService = new EducationLevelsService()

  implicit val edLevelFormat = Json.format[EducationLevel] 
  implicit val childFormat = Json.format[Child] 
  implicit val peopleFormat = Json.format[Person]
  implicit val authMethodFormat = Json.format[securesocial.core.AuthenticationMethod]
  implicit val oAuth1Format = Json.format[securesocial.core.OAuth1Info]
  implicit val oAuth2Format = Json.format[securesocial.core.OAuth2Info]
  implicit val passwordInfoFormat = Json.format[securesocial.core.PasswordInfo]

  implicit val secureUserFormat = Json.format[SecureUser]
  implicit val roleFormat = Json.format[Role]
  implicit val profileFormat = Json.format[Profile]

  def profile = SecuredAction { implicit request =>
    val roles = personService.findRoles(request.user.uid.get) 
    Ok(Json.toJson(new Profile(request.user, roles))) 
  }
  
  def children = SecuredAction{ implicit request => 
    var c = personService.findChildren(request.user.uid.get)
    var j = Json.obj("children" -> Json.toJson(c)) 
    Ok(j)
  }

  def child(id: Long) = SecuredAction{ implicit request =>
    personService.findChildren(request.user.uid.get) match {
      case children: List[Person] => {
        children.find{ child => child.id.get == id } match {
          case Some(c) => {
            educationLevelService.findByChild(c).map( e =>
              Ok(Json.toJson(new Child(c.id, c.firstName,c.lastName.get,c.birthDate.get.getMillis,Some(e))))
            ).getOrElse(
              Ok(Json.toJson(new Child(c.id,c.firstName,c.lastName.get,c.birthDate.get.getMillis,None)))
            )
          } 
          case None => BadRequest(Json.obj("status" -> "KO", "message" -> ("Unable to find child for " + request.user.firstName + " with an id of " + id.toString())))
        }
      }
      case _ => BadRequest(Json.obj("status" -> "KO", "message" -> ("Unable to find child for " + request.user.firstName + " with an id of " + id.toString())))
    }
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
            val p = new Person(None,person.firstName,Some(person.lastName),Some(new DateTime(person.birthDate)),None,None)
            val c = person.educationLevel match { 
              case Some(e) => personService.createPerson(p,e)
              case None => personService.createPerson(p)
            }
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
