package proximaltest.controllers;

import org.scalatestplus.play._
import play.api.Play.current
import play.api.db.slick.DB
import play.api.libs.json._
import play.api.mvc._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import proximaltest.helpers.{ ChildGenerator, SecureSocialHelper, StandardsHelpers }

class PersonControllerSpec extends PlaySpec with Results {

  import models._

  "Person Controller" should {

    "allow a logged in user to see their children" in {
      running(SecureSocialHelper.app) {
        val creds1 = cookies(route(FakeRequest(POST, "/authenticate/naive").withTextBody("user")).get)
        val Some(resp) = route(FakeRequest(GET, "/api/v1/children").withCookies(creds1.get("id").get))
        status(resp) mustEqual OK
        contentAsJson(resp) mustEqual Json.arr()
      }
    }

    "allow a logged in user to add a child" in {
      running(SecureSocialHelper.app) {
        DB.withSession { implicit s =>
          val e = EducationLevels.insert(StandardsHelpers.fakeEducationLevel)
          val Some(edLevel) = EducationLevels.find(e.id.get)
          edLevel.id.get mustEqual e.id.get
          val creds1 = cookies(route(FakeRequest(POST, "/authenticate/naive").withTextBody("user")).get)
          val resp = route(FakeRequest(POST, "/api/v1/children").withCookies(creds1.get("id").get).withJsonBody(ChildGenerator.child(edLevel))).get
          status(resp) mustEqual OK
        }
      }
    }
  }
}
