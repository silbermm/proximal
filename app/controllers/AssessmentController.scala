/* Copyright 2015 Matt Silbernagel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import play.Logger
import services._
import securesocial.core._

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{ Failure, Success }

case class ChildWithStandard(childId: Long, standardId: Long)

class AssessmentController(override implicit val env: RuntimeEnvironment[SecureUser]) extends securesocial.core.SecureSocial[SecureUser] {

  implicit val childWithStandardFormat = Json.format[ChildWithStandard]

  val newAssessmentActor = Akka.system.actorOf(Props[AssessmentActor])
  implicit val timeout = Timeout(30 seconds)

  def startAssessment = SecuredAction.async(BodyParsers.parse.json) { implicit request =>
    request.body.validate[ChildWithStandard].fold(
      errors => Future { BadRequest(Json.obj("message" -> s"Something went wrong!")) },
      obj => {
        val createAssessment = CreateAssessment(request.user.uid.get, obj.childId, obj.standardId)
        Logger.debug(s"$createAssessment")
        ask(newAssessmentActor, createAssessment).mapTo[Option[AssessmentQuestion]] map { message =>
          if (message.isEmpty) BadRequest(Json.obj("message" -> "nothing returned")) else Ok(Json.toJson(message))
        }
      }
    )
  }

  def scoreAssessment(assessmentId: Long) = SecuredAction.async(BodyParsers.parse.json) { implicit request =>
    request.body.validate[ScoreAssessment].fold(
      errors => Future { BadRequest(Json.obj("message" -> s"Something went wrong: $errors")) },
      obj => {
        ask(newAssessmentActor, obj).mapTo[Option[AssessmentQuestion]] map { message =>
          if (message.isEmpty) Ok else Ok(Json.toJson(message))
        }
      }
    )
  }

  def newAssesment = SecuredAction.async(BodyParsers.parse.json) { implicit request =>

    val webReq: ChildWithStandard = request.body.validate[ChildWithStandard].fold(
      errors => ChildWithStandard(-1, -1),
      obj => obj
    )

    if (webReq.childId < 1 || webReq.standardId < 1) {
      Future { BadRequest(Json.obj("message" -> s"Something went wrong! $webReq.childId")) }
    } else {
      PersonService.childActionAsync(request.user.uid.get, webReq.childId, c => {
        val childAndS = ChildAndStandard(c, webReq.standardId)

        ask(newAssessmentActor, childAndS).mapTo[Option[AssessmentQuestion]] map { x =>
          x match {
            case Some(obj) => {
              if (obj.question.id.get > 0)
                Ok(Json.toJson(obj))
              else
                BadRequest(Json.obj("message" -> "Unable to create the assessment, something bad happened."))
            }
            case None => BadRequest(Json.obj("message" -> "Sorry, unable to create the assessment."))
          }
        }
      })
    }

  }

  def newScore(assesmentId: Long) = SecuredAction(BodyParsers.parse.json) { implicit request =>
    // Make sure assessment exists and get the childId that belongs to this assessment
    val childId = AssesmentService.findChildForAssessment(assesmentId);
    if (childId < 1) {
      BadRequest(Json.obj("message" -> s"assessment not found"))
    } else {
      PersonService.childAction(request.user.uid.get, childId, c => {
        request.body.validate[Score].fold(
          errors => BadRequest(Json.obj("message" -> JsError.toFlatJson(errors))),
          qscore => {
            val q = AssesmentService.createQuestionScore(assesmentId, qscore, childId)
            Ok(Json.obj("assesment" -> Json.toJson(q._1), "question" -> Json.toJson(q._2)))
          }
        )
      })
    }
  }
}
