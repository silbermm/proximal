package models


import play.api.db.slick.DB
import play.api.Play.current

import collection.mutable.Stack
import scala.concurrent.Future
import org.scalatest._
import org.scalatestplus.play._

import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._

import securesocial.core._
import securesocial.core.services._

class UserSpec extends PlaySpec with Results {
  
  import models._

  "User model" should {
    "insert and retrieve new record by id" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession{ implicit s =>
          val pwordInfo = new PasswordInfo("bcrypt", "9090909090", None)
          val authMethod = new AuthenticationMethod("userPassword")
          
          val u = new SecureUser(uid = None, providerId = "userpass", 
                                 userId = "silbermm", firstName =Some("Matt"), lastName =Some("Silbernagel"), 
                                 fullName=Some("Matt Silbernagel"), email = Some("silbermm@gmail.com"), avatarUrl = None, 
                                 authMethod = authMethod, oAuth1Info=None, oAuth2Info=None, passwordInfo = Some(pwordInfo) 
                                 )
          SecureUsers.save(ProfileFromUser(u), SaveMode.LoggedIn);
          val Some(us) = SecureUsers.findById(1)
          us.firstName mustEqual "Matt"
        }
      }
    }
  }
  
  object ProfileFromUser {
    def apply(user: SecureUser): BasicProfile = {
      BasicProfile(user.userId, user.providerId, user.firstName, user.lastName, user.fullName,user.email,user.avatarUrl,user.authMethod,user.oAuth1Info,user.oAuth2Info,user.passwordInfo)
    } 
  }  

}
