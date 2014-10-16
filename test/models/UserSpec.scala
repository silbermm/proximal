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

import play.api.Logger

import helpers._

class UserSpec extends PlaySpec with Results {
  
  import models._

  "User model" should {
    "insert and retrieve new record by id" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession{ implicit s =>
          val u = UserProfileHelpers.fakeUser
          SecureUsers.save(UserProfileHelpers.profileFromUser(u), SaveMode.LoggedIn)
          SecureUsers.findById(1) match {
            case Some(us) => us.firstName.get mustEqual "Matt"
            case None     => fail()
          }
        }
      }
    }

    "update the password info" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession{ implicit s =>
          val u = UserProfileHelpers.fakeUser
          SecureUsers.save(UserProfileHelpers.profileFromUser(u), SaveMode.LoggedIn) 
          
          val savedUser = SecureUsers.findById(1) match {
            case Some(us) => us
            case None    => fail("unable to find a user to update")
          }
          
          val pword = new PasswordInfo("bcrypt", "new", None)
          val uid = SecureUsers.updatePasswordInfo(savedUser, pword) match {
            case Some(us) => us.uid.get
            case None     => fail("the update failed to return a user id") 
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
