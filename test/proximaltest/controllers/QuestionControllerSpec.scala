package proximaltest.controllers;

import org.scalatestplus.play._
import play.api.mvc._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import proximaltest.helpers._

class QuestionControllerSpec extends PlaySpec with Results {

  "Question Controller" should {
    "not allow a non admin to create a new question" in {
      running(SecureSocialHelper.app) {
        val creds1 = cookies(route(FakeRequest(POST, "/authenticate/naive").withTextBody("user")).get)
        val qu = QuestionGenerator.question
        val Some(resp) = route(FakeRequest(POST, "/api/v1/questions").withCookies(creds1.get("id").get).withJsonBody(QuestionGenerator.question))
        status(resp) mustEqual UNAUTHORIZED
      }
    }
  }

}
