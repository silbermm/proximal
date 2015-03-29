package controllers

import services._
import models._
import helpers.ImplicitJsonFormat._

import play.api.mvc._
import play.api.Play.current
import play.api.Logger
import play.api.libs.json._
import play.api.libs.concurrent.Akka

import securesocial.core._

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration._
import scala.concurrent.Future
import scala.language.postfixOps

case class ChildAndActivity(childId: Long, statementId: Long, activity: Activity, homework: Homework, acts: List[Act])

class ActivityController(override implicit val env: RuntimeEnvironment[SecureUser]) extends securesocial.core.SecureSocial[SecureUser] {

  val activityActor = Akka.system.actorOf(Props[ActivityActor])
  implicit val timeout = Timeout(30 seconds)

  implicit val childAndActivityFormat = Json.format[ChildAndActivity]

  def listActivities = SecuredAction.async { implicit request =>
    try {
      ask(activityActor, ListActivities(request.user.uid.get)).mapTo[List[Activity]] map { x =>
        Ok(Json.toJson(x))
      }
    } catch {
      case e: Throwable => Future { BadRequest(Json.obj("message" -> s"$e.getMessage()")) }
    }
  }

  def listAllActivities = SecuredAction.async { implicit request =>
    try {
      ask(activityActor, ListActivities(0)).mapTo[List[Activity]] map { x =>
        Ok(Json.toJson(x))
      }
    } catch {
      case e: Throwable => Future { BadRequest(Json.obj("message" -> s"$e.getMessage()")) }
    }
  }

  def createActivity = SecuredAction.async(BodyParsers.parse.json) { implicit request =>
    //Todo: Make sure this person has the correct permissions to do this! 
    request.body.validate[CreateActivity].fold(
      errors => { Future { BadRequest(Json.obj("message" -> s"$errors")) } },
      obj => {
        try {
          ask(activityActor, obj).mapTo[Option[Activity]] map { x =>
            x match {
              case Some(a) => Ok(Json.toJson(a))
              case None => BadRequest(Json.obj("message" -> "Sorry, unable to create the activity"))
            }
          }
        } catch {
          case e: Throwable => { Future { BadRequest(Json.obj("message" -> e.getMessage)) } }
        }
      })
  }

  def allActivitySets = SecuredAction.async { implicit request =>
    ???
  }

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
        val activity = obj.activity.copy(creator = request.user.uid.get)
        val childAndActivity = obj.copy(activity = activity)
        createHomework(childAndActivity)
      }
    )
  }

  def allHomework(childId: Long) = SecuredAction.async { implicit request =>
    PersonService.childActionAsync(request.user.uid.get, childId, c => {
      ask(activityActor, ListHomework(c)).mapTo[List[HomeworkActivityActs]] map { x =>
        Ok(Json.toJson(x))
      }
    })
  }

  def deleteHomework(homeworkId: Long) = SecuredAction.async { implicit request =>
    ask(activityActor, DeleteHomework(request.user.uid.get, homeworkId)).mapTo[Option[Int]] map { x =>
      var num = x.getOrElse(0);
      if (num > 0) {
        Ok(Json.obj("message" -> "success"))
      } else {
        BadRequest(Json.obj("message" -> "something went wrong"));
      }
    }
  }

  def deleteActivity(activityId: Long) = SecuredAction.async { implicit request =>
    ask(activityActor, DeleteActivity(activityId)) map { message =>
      message match {
        case deleted: Int => Ok
        case e: Throwable => BadRequest(Json.obj("message" -> s"$e"))
      }
    }
  }

  def updateAct = SecuredAction.async(BodyParsers.parse.json) { implicit request =>
    Future.successful { Ok("done") }
  }

}
