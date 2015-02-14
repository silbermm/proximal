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

import play.api.Logger
import helpers._

class ActSpec extends PlaySpec with Results {
  import models._

  "Act model" should {

    "create an act" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val sampleAct = ActHelpers.sampleAct
          val act = Acts.create(sampleAct)
          act.id must not be empty
          act.actType mustEqual sampleAct.actType
        }
      }
    }

    "find an act" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val sampleAct = ActHelpers.sampleAct
          val act = Acts.create(sampleAct)
          act.id must not be empty
          act.actType mustEqual sampleAct.actType
          Acts.find(act.id.get) match {
            case Some(a) => a.id.get mustEqual act.id.get
            case None => fail("unable to find the act in the database :(")
          }
        }
      }
    }

    "list all acts" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          Acts.create(ActHelpers.sampleAct)
          Acts.create(ActHelpers.sampleAct)
          Acts.all must not be empty
          Acts.all must have length 2
        }
      }
    }

    "delete an act" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val sampleAct = ActHelpers.sampleAct
          val act = Acts.create(sampleAct)
          act.id must not be empty
          act.actType mustEqual sampleAct.actType
          val deleted = Acts.delete(act)
          deleted mustBe 1
        }
      }
    }
  }
}
