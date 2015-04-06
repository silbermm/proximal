package proximaltest.controllers;

import org.scalatestplus.play._
import play.api.libs.json._
import play.api.mvc._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import proximaltest.helpers._

class ResourceControllerSpec extends PlaySpec with Results {
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
