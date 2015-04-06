package controllers

import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout
import helpers.ImplicitJsonFormat._
import models._
import play.api.Play.current
import play.api.libs.concurrent.Akka
import play.api.libs.json._
import play.api.mvc._
import services._
import securesocial.core._

import scala.concurrent.Future
import scala.concurrent.duration._

class ResourceController(override implicit val env: RuntimeEnvironment[SecureUser]) extends securesocial.core.SecureSocial[SecureUser] {

  val resourceActor = Akka.system.actorOf(Props[ResourceActor])
  implicit val timeout = Timeout(15 seconds)

  def listResources = SecuredAction.async { implicit request =>
    ask(resourceActor, ListResources) map { message =>
      message match {
        case res: List[Resource] => Ok(Json.toJson(res))
        case ex: Exception => BadRequest(Json.obj("message" -> ex.getMessage()))
      }
    }
  }

  def createResource = SecuredAction.async(BodyParsers.parse.json) { implicit request =>
    request.body.validate[Resource].fold(
      errors => { Future { BadRequest(Json.obj("message" -> s"$errors")) } },
      obj => {
        ask(resourceActor, CreateResource(obj, Some(request.user.uid.get))) map { message =>
          message match {
            case res: Resource => Ok(Json.toJson(res))
            case ex: Exception => BadRequest(Json.obj("message" -> ex.getMessage()))
          }
        }
      })
  }

  def deleteResource = SecuredAction.async { implicit request =>
    ???
  }

  def getResource(id: Long) = SecuredAction.async { implicit request =>
    ask(resourceActor, GetResource(id)) map { message =>
      message match {
        case Some(res: Resource) => Ok(Json.toJson(res))
        case ex: Exception => BadRequest(Json.obj("message" -> ex.getMessage()))
      }
    }
  }

}
