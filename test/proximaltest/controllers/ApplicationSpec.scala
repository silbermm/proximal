package proximaltest.controllers;

import java.lang.reflect.Constructor
import services.SecureUserService
import play.api.GlobalSettings
import play.api.test.{ FakeRequest, WithApplication, FakeApplication, PlaySpecification }
import securesocial.core.RuntimeEnvironment

import play.api.Play.current

import collection.mutable.Stack
import scala.concurrent.Future
import org.scalatest._
import org.scalatestplus.play._

import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._

import play.api.Logger
import proximaltest.helpers._

class ApplicationSpec extends PlaySpec with Results {

  import models._
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
