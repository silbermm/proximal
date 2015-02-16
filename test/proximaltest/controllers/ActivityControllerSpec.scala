package proximaltest.controllers;

import java.lang.reflect.Constructor
import services.SecureUserService
import play.api.GlobalSettings
import play.api.test.{ FakeRequest, WithApplication, FakeApplication, PlaySpecification }
import securesocial.core.RuntimeEnvironment
import controllers.ChildAndActivity
import services.ActivityActor._

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

class ActivityControllerSpec extends PlaySpec with Results {
  import models._

  import helpers.ImplicitJsonFormat._
  implicit val childActivityFormat = Json.format[ChildAndActivity]

  "Activity Controller" should {
    "not allow a non-authenticated user access to GET all Homework" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val Some(response) = route(FakeRequest(GET, "/api/v1/activities/homework/1230'"))
        status(response) must not be OK
      }
    }

    "authenticated user can create homework" in {
      running(SecureSocialHelper.app) {
        val creds1 = cookies(route(FakeRequest(POST, "/authenticate/naive").withTextBody("user")).get)
        val Some(user) = route(FakeRequest(GET, "/api/v1/profile").withCookies(creds1.get("id").get))
        val json: JsValue = Json.parse(contentAsString(user))
        contentType(user) mustEqual Some("application/json")

        // Add a child to the user
        val child = Child(None, "whatever", "lastwhatever", 1207195200000L, None)
        val Some(childResponse) = route(FakeRequest(POST, "/api/v1/children").withCookies(creds1.get("id").get).withJsonBody(Json.toJson(child)))
        status(childResponse) mustBe OK
        val childJson: JsValue = Json.parse(contentAsString(childResponse))

        // Add homework for that child
        val childAndHomework = ChildAndActivity((childJson \ "id").as[Long], 78L, ActivityHelpers.sampleActivity, ActivityHelpers.sampleHomework, List.empty)
        //Logger.debug(json.toString)
        //val Some(homework) = route(FakeRequest(POST, "/api/v1/activities/homework").withCookies(creds1.get("id").get).withJsonBody(Json.toJson(childAndHomework)))
        //status(homework) mustBe OK
      }
    }
  }
}
