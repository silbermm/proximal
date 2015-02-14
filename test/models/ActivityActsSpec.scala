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

class ActivityActsSpec extends PlaySpec with Results {
  import models._

  "ActivityAct model" should {

    "create an activityact" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val activity = Activities.create(ActivityHelpers.sampleActivity)
          val act = Acts.create(ActHelpers.sampleAct)
          val activityAct = ActivityActs.create(ActivityAct(None, activity.id.get, act.id.get))
          activityAct.activityId mustEqual activity.id.get
          activityAct.actId mustEqual act.id.get
        }
      }
    }

    "find an activityAct" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val activity = Activities.create(ActivityHelpers.sampleActivity)
          val act = Acts.create(ActHelpers.sampleAct)
          val activityAct = ActivityActs.create(ActivityAct(None, activity.id.get, act.id.get))
          ActivityActs.find(activityAct.id.get) match {
            case Some(a) => a.id.get mustEqual activityAct.id.get
            case None => fail("unable to find the activityAct :(");
          }
        }
      }

    }

    "list all activityActs" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val activity = Activities.create(ActivityHelpers.sampleActivity)
          val act = Acts.create(ActHelpers.sampleAct)
          val activityAct = ActivityActs.create(ActivityAct(None, activity.id.get, act.id.get))
          ActivityActs.all must have length 1
        }
      }

    }

    "delete an activityAct" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val activity = Activities.create(ActivityHelpers.sampleActivity)
          val act = Acts.create(ActHelpers.sampleAct)
          val activityAct = ActivityActs.create(ActivityAct(None, activity.id.get, act.id.get))
          val deleted = ActivityActs.delete(activityAct)
          deleted mustEqual 1
          ActivityActs.all must have length 0
        }
      }

    }

  }
}
