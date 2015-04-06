package models

import org.scalatestplus.play._
import play.api.Logger
import play.api.Play.current
import play.api.db.slick.DB
import play.api.mvc._
import play.api.test.Helpers._
import play.api.test._
import securesocial.core._
import securesocial.core.services._

//import helpers._
import proximaltest.helpers._

class UserSpec extends PlaySpec with Results {

  "User model" should {
    "insert and retrieve new record by id" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val u = UserProfileHelpers.fakeUser
          val saved = SecureUsers.save(UserProfileHelpers.profileFromUser(u), SaveMode.LoggedIn)
          SecureUsers.findById(saved.uid.get) match {
            case Some(us) => us.firstName.get mustEqual "Matt"
            case None => fail()
          }
        }
      }
    }

    "update the password info" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val u = UserProfileHelpers.fakeUser
          val savedUser = SecureUsers.save(UserProfileHelpers.profileFromUser(u), SaveMode.LoggedIn)

          val pword = new PasswordInfo("bcrypt", "new", None)
          val uid = SecureUsers.updatePasswordInfo(savedUser, pword) match {
            case Some(us) => us.uid.get
            case None => fail("the update failed to return a user id")
          }
          Logger.debug("uid = " + uid)
          SecureUsers.findById(uid) match {
            case Some(us) => us.passwordInfo.get.password mustEqual "new"
            case None => fail()
          }
        }
      }
    }
  }
}
