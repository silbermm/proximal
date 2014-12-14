package helpers

import java.lang.reflect.Constructor
import services.SecureUserService
import play.api.GlobalSettings
import play.api.test.{FakeRequest, WithApplication, FakeApplication, PlaySpecification}
import securesocial.core.RuntimeEnvironment

import play.api.test._
import play.api.test.Helpers._


import play.api.Logger
import models._

object SecureSocialHelper {


  def app = FakeApplication( additionalConfiguration = inMemoryDatabase(), withGlobal=Some(global(env)))
 
  val env=new AlwaysValidIdentityProvider.RuntimeEnvironment[SecureUser] {
    override lazy val userService: SecureUserService = new SecureUserService()
  }

  def global[A](env:RuntimeEnvironment[A]): GlobalSettings =
    new play.api.GlobalSettings {
      override def getControllerInstance[A](controllerClass: Class[A]): A = {
        val instance = controllerClass.getConstructors.find { c =>
          val params = c.getParameterTypes
          params.length == 1 && params(0) == classOf[RuntimeEnvironment[A]]
        }.map {
          _.asInstanceOf[Constructor[A]].newInstance(env)
        }
        instance.getOrElse(super.getControllerInstance(controllerClass))
      }
    }
}
