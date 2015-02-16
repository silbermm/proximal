package controllers

import services._
import models._
import helpers.ImplicitJsonFormat._

import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.Logger
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.libs.concurrent.Akka

import securesocial.core._

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration._
import scala.concurrent.Future

case class ChildAndActivity(childId: Long, statementId: Long, activity: Activity, homework: Homework, acts: List[Act])

class ActivityController(override implicit val env: RuntimeEnvironment[SecureUser]) extends securesocial.core.SecureSocial[SecureUser] {

  val activityActor = Akka.system.actorOf(Props[ActivityActor])
  implicit val timeout = Timeout(30 seconds)

  implicit val childAndActivityFormat = Json.format[ChildAndActivity]

  def newHomeworkActivity = SecuredAction.async(BodyParsers.parse.json) { implicit request =>

    def createHomework(childAndActivity: ChildAndActivity): Future[Result] = {
      PersonService.childActionAsync(request.user.uid.get, childAndActivity.childId, c => {
        val hwact = CreateHomeworkActivity(List(childAndActivity.statementId), childAndActivity.activity, childAndActivity.homework, childAndActivity.acts)
        ask(activityActor, hwact).mapTo[Option[CreateHomeworkActivity]] map { x =>
          x match {
            case Some(obj) => Ok(Json.toJson(obj))
            case None => BadRequest(Json.obj("message" -> "Sorry, unable to create add the homework."))
          }
        }
      })
    }

    request.body.validate[ChildAndActivity].fold(
      errors => { Future { BadRequest(Json.obj("message" -> s"Something went wrong! $errors")) } },
      obj => {
        val activity = obj.activity.copy(creator = Some(request.user.userId))
        val childAndActivity = obj.copy(activity = activity)
        createHomework(childAndActivity)
      }
    )

  }

  def allHomework(childId: Long) = SecuredAction.async { implicit request =>

    PersonService.childActionAsync(request.user.uid.get, childId, c => {
      Future { Ok }
    })
  }

}
