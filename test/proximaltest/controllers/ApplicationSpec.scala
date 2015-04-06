package proximaltest.controllers;

import org.scalatestplus.play._
import play.api.mvc._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import proximaltest.helpers._

class ApplicationSpec extends PlaySpec with Results {
  "AppliationController" should {

    "allow a logged in user to view the index page" in {
      running(SecureSocialHelper.app) {
        val creds1 = cookies(route(FakeRequest(POST, "/authenticate/naive").withTextBody("user")).get)
        val Some(response) = route(FakeRequest(GET, "/").withCookies(creds1.get("id").get))
        status(response) mustEqual OK
      }
    }
  }

}
