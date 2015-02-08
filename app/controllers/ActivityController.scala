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

case class ChildAndActivity(childId: Long, activity: Activity, homework: Homework)

class ActivityController(override implicit val env: RuntimeEnvironment[SecureUser]) extends securesocial.core.SecureSocial[SecureUser] {

  val activityActor = Akka.system.actorOf(Props[ActivityActor])
  implicit val timeout = Timeout(30 seconds)

  implicit val childAndActivityFormat = Json.format[ChildAndActivity]

  def newHomeworkActivity = SecuredAction.async(BodyParsers.parse.json) { implicit request =>

    request.body.validate[ChildAndActivity].fold(
      errors => { Future { BadRequest(Json.obj("message" -> s"Something went wrong! $errors.toString()")) } },
      obj => { Future { Ok(Json.obj("message" -> s"Good Request")) } }
    )

    // here create the method when everything is good

  }

}