package controllers

import collection.mutable.Stack
import scala.concurrent.Future
import org.scalatest._
import org.scalatestplus.play._

import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._

import controllers._
//import controllers.RegistrationData

class AccountSpec extends PlaySpec with Results {
  
  class TestController() extends Controller with AccountController

  "AccountController#create" should  {
    "be valid" in  {
      val controller = new TestController()
      val result: Future[Result] = controller.create().apply(FakeRequest())
      status(result) mustEqual OK
    }
  }

  "AccountController#createPost" should {
    "create account" in {
      val controller = new TestController()
      val f = AccountController.registrationForm.fill(RegistrationData("Matt", "Sil", "silbermm@gmail.com","pass","pass"))
      val result : Future[Result] = controller.createPost().apply(FakeRequest("POST", "/account/create", new FakeHeaders(),RegistrationData("Matt", "Sil", "silbermm@gmail.com","pass","pass")))
      true mustBe true
    }
  }
}
