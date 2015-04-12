package proximaltest.controllers;

import controllers.ChildAndActivity
import org.scalatestplus.play._
import play.api.libs.json._
import play.api.mvc._
import play.api.test.Helpers._
import play.api.test.{ FakeApplication, FakeRequest }
import proximaltest.helpers._
import services._
import play.api.Play.current
import play.api.db.slick.DB

import scala.compat.Platform._

class ActivityControllerSpec extends PlaySpec with Results {
  import helpers.ImplicitJsonFormat._
  import models._
  implicit val childActivityFormat = Json.format[ChildAndActivity]

  "Activity Controller" should {

    "allow an authenticated user to create an activity" in {
      running(SecureSocialHelper.app) {
        val creds1 = cookies(route(FakeRequest(POST, "/authenticate/naive").withTextBody("user")).get)
        val Some(user) = route(FakeRequest(GET, "/api/v1/profile").withCookies(creds1.get("id").get))
        val json: JsValue = Json.parse(contentAsString(user))
        contentType(user) mustEqual Some("application/json")
        val uid = (json \ "user" \ "uid").as[Long]
        val createActivity = CreateActivity(ActivityHelpers.sampleActivity.copy(creator = uid), List.empty)
        val Some(create) = route(FakeRequest(POST, "/api/v1/activities").withJsonBody(Json.toJson(createActivity)).withCookies(creds1.get("id").get))
        status(create) mustBe OK
      }
    }

    "not allow a non-authenticated user access to GET all Homework" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val Some(response) = route(FakeRequest(GET, "/api/v1/activities/homework/1230'"))
        status(response) must not be OK
      }
    }

    "allow a parent to get a question for their student" in {
      running(SecureSocialHelper.app) {
        val (parentId, childId, edLevel, creds) = Fixtures.setupUserAndChild
        val (standard, statement) = Fixtures.setupStandardsAndStatements(edLevel)
        val (resource1, question1) = Fixtures.setupQuestions(parentId)
        val (resource2, question2) = Fixtures.setupQuestions(parentId)
        val activity = Fixtures.setupActivity(parentId, standard, statement, resource1)
        val Some(questionResponse) = route(FakeRequest(GET, s"/api/v1/questions/student/$childId/standard/${standard.id.get}")
          .withCookies(creds.get("id").get))
        status(questionResponse) mustBe OK
        val questionObj = Json.parse(contentAsString(questionResponse)).as[ActivityQuestion]
        questionObj.activity.id must not be empty
        questionObj.question.id must not be empty
      }
    }

    "allow a parent to create an attempt for their student and activity" in {
      running(SecureSocialHelper.app) {
        // setup  
        val (parentId, childId, edLevel, creds) = Fixtures.setupUserAndChild
        val (standard, statement) = Fixtures.setupStandardsAndStatements(edLevel)
        val (resource1, question1) = Fixtures.setupQuestions(parentId)
        val (resource2, question2) = Fixtures.setupQuestions(parentId)

        // create two activities
        val activity = Fixtures.setupActivity(parentId, standard, statement, resource1)
        val activity2 = Fixtures.setupActivity(parentId, standard, statement, resource2)

        val Some(questionResponse) = route(FakeRequest(GET, s"/api/v1/questions/student/$childId/standard/${standard.id.get}")
          .withCookies(creds.get("id").get))
        status(questionResponse) mustBe OK
        val questionObj = Json.parse(contentAsString(questionResponse)).as[ActivityQuestion]
        // Create the json attempt response to send back to the server 
        val attempt = Json.toJson(Attempt(None, questionObj.activity.id.get, childId, currentTime, 5))
        val Some(attemptResponse) = route(FakeRequest(POST, s"/api/v1/attempts").withJsonBody(attempt).withCookies(creds.get("id").get))
        status(attemptResponse) mustBe OK

        // Another call to get a question should not be the same question we just got.... 
        val Some(questionResponse2) = route(FakeRequest(GET, s"/api/v1/questions/student/$childId/standard/${standard.id.get}")
          .withCookies(creds.get("id").get))
        val questionObj2 = Json.parse(contentAsString(questionResponse2)).as[ActivityQuestion]

        questionObj2 must not be questionObj
      }
    }

    "allow authenticated user to create homework" in {
      running(SecureSocialHelper.app) {
        val creds1 = cookies(route(FakeRequest(POST, "/authenticate/naive").withTextBody("user")).get)
        val Some(user) = route(FakeRequest(GET, "/api/v1/profile").withCookies(creds1.get("id").get))
        val json: JsValue = Json.parse(contentAsString(user))
        contentType(user) mustEqual Some("application/json")
        val uid = (json \ "user" \ "uid").as[Long]
        // Add a child to the user
        val child = Child(None, "whatever", "lastwhatever", 1207195200000L, None)
        val Some(childResponse) = route(FakeRequest(POST, "/api/v1/children").withCookies(creds1.get("id").get).withJsonBody(Json.toJson(child)))
        status(childResponse) mustBe OK
        val childJson: JsValue = Json.parse(contentAsString(childResponse))
        // Add homework for that child
        val childAndHomework = ChildAndActivity((childJson \ "id").as[Long], 78L, ActivityHelpers.sampleActivity.copy(creator = uid), ActivityHelpers.sampleHomework, List.empty)
      }
    }
  }
}
