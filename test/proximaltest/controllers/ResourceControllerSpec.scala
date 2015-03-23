package proximaltest.controllers;

import java.lang.reflect.Constructor
import services.SecureUserService
import play.api.GlobalSettings
import play.api.test.{ FakeRequest, WithApplication, FakeApplication, PlaySpecification }
import securesocial.core.RuntimeEnvironment
import controllers.ChildAndActivity
import services._

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
import proximaltest.helpers._

import play.api.db.slick.DB
import play.api.Play.current

import play.api.libs.json._

class ResourceControllerSpec extends PlaySpec with Results {
  import models._
  import helpers.ImplicitJsonFormat._

  def setupUser = {
    val creds1 = cookies(route(FakeRequest(POST, "/authenticate/naive").withTextBody("user")).get)
    val Some(user) = route(FakeRequest(GET, "/api/v1/profile").withCookies(creds1.get("id").get))
    val json: JsValue = Json.parse(contentAsString(user))
    val uid = (json \ "user" \ "uid").as[Long]
    creds1
  }

  "Resource Controller" should {
    "allow an authenticated user to create a resource" in {
      running(SecureSocialHelper.app) {
        val creds = setupUser
        val sampleResource = ResourceHelpers.sampleResource
        val Some(resource) = route(FakeRequest(POST, "/api/v1/resources").withJsonBody(Json.toJson(sampleResource)).withCookies(creds.get("id").get))
        status(resource) mustBe OK
      }
    }

    "allow an authenticated user to list all resources" in {
      running(SecureSocialHelper.app) {
        val creds = setupUser
        //val sampleResource = ResourceHelpers.sampleResource
        val Some(resource) = route(FakeRequest(GET, "/api/v1/resources").withCookies(creds.get("id").get))
        status(resource) mustBe OK
      }
    }
  }

}
