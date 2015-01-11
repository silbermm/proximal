package controllers;

import java.lang.reflect.Constructor
import services.SecureUserService
import play.api.GlobalSettings
import play.api.test.{FakeRequest, WithApplication, FakeApplication, PlaySpecification}
import securesocial.core.RuntimeEnvironment

import securesocial.core._
import securesocial.core.services._

import collection.mutable.Stack
import scala.concurrent.Future
import org.scalatest._
import org.scalatestplus.play._

import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._

import play.api.libs.json._

import play.api.Logger
import helpers._

import play.api.db.slick.DB
import play.api.Play.current


class QuestionControllerSpec extends PlaySpec with Results {
  import models._


  "Question Controller" should {
    "not allow a non admin to create a new question" in {
      running(SecureSocialHelper.app){
        val creds1 = cookies(route(FakeRequest(POST, "/authenticate/naive").withTextBody("user")).get)
        val qu = QuestionGenerator.question
        val Some(resp) = route(FakeRequest(POST, "/api/v1/questions").withCookies(creds1.get("id").get).withJsonBody(QuestionGenerator.question))
        status(resp) mustEqual UNAUTHORIZED
      }
    } 
  }

}
