package proximaltest.models

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
import proximaltest.helpers._

class ActivitySetsSpec extends PlaySpec with Results {
  import models._

  "ActivitySet Model" should {
    "create an ActivitySet" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          // We need both a set and an activity
          val aset = Sets.create(SetHelpers.sampleSet)
          aset.id must not be empty

          val sampleActivity = ActivityHelpers.sampleActivity
          val activity = Activities.create(sampleActivity)
          activity.id must not be empty

          val activitySet = ActivitySets.create(ActivitySet(None, activity.id.get, aset.id.get))
          activitySet.id must not be empty
        }
      }
    }

    "find an activity set" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          // We need both a set and an activity
          val aset = Sets.create(SetHelpers.sampleSet)
          aset.id must not be empty

          val sampleActivity = ActivityHelpers.sampleActivity
          val activity = Activities.create(sampleActivity)
          activity.id must not be empty

          val activitySet = ActivitySets.create(ActivitySet(None, activity.id.get, aset.id.get))
          activitySet.id must not be empty

          ActivitySets.find(activitySet.id.get) match {
            case Some(as) => as.activityId mustEqual activity.id.get
            case _ => fail("unable to find the created activity set")
          }
        }
      }

    }
  }

}
