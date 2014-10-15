package controllers

import collection.mutable.Stack
import scala.concurrent.Future
import org.scalatest._
import org.scalatestplus.play._

import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._

import controllers._

import securesocial.testkit.WithLoggedUser

class ApplicationSpec extends PlaySpec with Results {
  
  import WithLoggedUser._

  def minimalApp = FakeApplication(withoutPlugins=excludedPlugins,additionalPlugins = includedPlugins)

  "ApplicationController#index" should  {
    "should be valid" in new WithLoggedUser(minimalApp) {

      val req: Request[AnyContent] = FakeRequest().
        withHeaders((HeaderNames.CONTENT_TYPE, "application/x-www-form-urlencoded")).
        withCookies(cookie) // Fake cookie from the WithloggedUser trait

      val result = Application.index.apply(req)
      //val result: Future[Result] = controller.index().apply(FakeRequest(additionalPlugins = Seq("securesocial.testkit.AlwaysValidIdentityProvider")))
      status(result) mustEqual OK
    }
  }
}
