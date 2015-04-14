package controllers

import models._
import org.joda.time.DateTime
import play.api._
import play.api.libs.json._
import play.api.mvc._
import services._
import securesocial.core._

class PersonController(override implicit val env: RuntimeEnvironment[SecureUser]) extends securesocial.core.SecureSocial[SecureUser] {

  val personService = new PersonService()
  val educationLevelService = new EducationLevelsService()

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
    val person = personService.findPersonByUid(request.user.uid.get)
    Ok(Json.toJson(new Profile(request.user, person, roles)))
  }

  def children = SecuredAction { implicit request =>
    val c = personService.findChildren(request.user.uid.get)
    Ok(Json.toJson(c))
  }

  def child(id: Long) = SecuredAction { implicit request =>
    personService.findChildren(request.user.uid.get) match {
      case children: List[Person] => {
        children.find { child => child.id.getOrElse(0L) == id } match {
          case Some(c) => {
            educationLevelService.findByChild(c).map(e =>
              Ok(Json.toJson(new Child(c.id, c.firstName, c.lastName.get, c.birthDate.map(_.getMillis).getOrElse(0L), Some(e))))
            ).getOrElse(
              Ok(Json.toJson(new Child(c.id, c.firstName, c.lastName.get, c.birthDate.map(_.getMillis).getOrElse(0L), None)))
            )
          }
          case None => {
            BadRequest(Json.obj("status" -> "KO", "message" ->
              ("Unable to find child for " + request.user.firstName + " with an id of " + id.toString()))
            )
          }
        }
      }
      case _ => BadRequest(Json.obj("status" -> "KO", "message" -> ("Unable to find child for " + request.user.firstName + " with an id of " + id.toString())))
    }
  }

  def addChild = SecuredAction(BodyParsers.parse.json) { implicit request =>
    Logger.debug("Validating the Json sent it")
    val childToAdd = request.body.validate[Child]
    childToAdd.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(errors)))
      },
      person => {
        personService.findPersonByUid(request.user.uid.get) match {
          case Some(pr) => {
            val p = new Person(None, person.firstName, Some(person.lastName), Some(new DateTime(person.birthDate)), None, None)
            Logger.debug("Checking if education level was sent in")
            val c = person.educationLevel match {
              case Some(e) => personService.createPerson(p, e)
              case _ => personService.createPerson(p)
            }
            personService.addChild(c, pr)
            Ok(Json.toJson(c))
          }
          case _ => BadRequest
        }
      }
    )
  }

  def removeChild(id: Long) = SecuredAction { implicit request =>
    personService.findChildren(request.user.uid.get) match {
      case children: List[Person] => {
        Logger.debug(children.toString())
        children.find { child => child.id.getOrElse(0L) == id } match {
          case Some(c) => {
            personService.deletePerson(id)
            Ok(Json.toJson(c))
          }
          case None => BadRequest(Json.obj("status" -> "KO", "message" -> ("No match in the list for id " + id.toString())))
        }
      }
      case _ => BadRequest(Json.obj("status" -> "KO", "message" -> ("Unable to find child for " + request.user.firstName + " with an id of " + id.toString())))
    }
  }

}
