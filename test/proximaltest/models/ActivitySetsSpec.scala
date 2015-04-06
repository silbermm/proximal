package proximaltest.models

import org.scalatestplus.play._
import play.api.Play.current
import play.api.db.slick.DB
import play.api.mvc._
import play.api.test.Helpers._
import play.api.test._
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

          val person = People.insertPerson(PersonHelpers.person)
          val sampleActivity = ActivityHelpers.sampleActivity.copy(creator = person.id.get)
          val activity = Activities.create(sampleActivity)
          activity.id must not be empty

          val activitySet = ActivitySets.create(ActivitySet(None, activity.id.get, aset.id.get, None))
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

          val person = People.insertPerson(PersonHelpers.person)
          val sampleActivity = ActivityHelpers.sampleActivity.copy(creator = person.id.get)
          val activity = Activities.create(sampleActivity)
          activity.id must not be empty

          val activitySet = ActivitySets.create(ActivitySet(None, activity.id.get, aset.id.get, None))
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
