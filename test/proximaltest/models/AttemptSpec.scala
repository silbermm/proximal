package proximaltest.models

import org.scalatestplus.play._
import play.api.Play.current
import play.api.db.slick.DB
import play.api.mvc._
import play.api.test.Helpers._
import play.api.test._
//import helpers._
import proximaltest.helpers._

import scala.compat.Platform._

class AttemptSpec extends PlaySpec with Results {
  import models._

  "Attempt model" should {

    "create an attempt" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val person = People.insertPerson(PersonHelpers.person)
          val student = People.insertPerson(PersonHelpers.person)
          val sampleActivity = ActivityHelpers.sampleActivity.copy(creator = person.id.get)
          val activity = Activities.create(sampleActivity)
          activity.id must not be empty

          var attempt = Attempt(None, activity.id.get, student.id.get, currentTime, 5L)
          var created = Attempts.create(attempt);
          created.id must not be empty
        }
      }
    }

    "delete an attempt" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val person = People.insertPerson(PersonHelpers.person)
          val student = People.insertPerson(PersonHelpers.person)
          val sampleActivity = ActivityHelpers.sampleActivity.copy(creator = person.id.get)
          val activity = Activities.create(sampleActivity)
          activity.id must not be empty

          var attempt = Attempt(None, activity.id.get, student.id.get, currentTime, 5L)
          var created = Attempts.create(attempt);
          created.id must not be empty

          var deleted = Attempts.delete(created);
          deleted mustEqual 1
        }
      }
    }

    "list all attempts" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>

          val person = People.insertPerson(PersonHelpers.person)
          val sampleActivity = ActivityHelpers.sampleActivity.copy(creator = person.id.get)
          val sampleActivity2 = ActivityHelpers.sampleActivity.copy(creator = person.id.get)

          Activities.create(sampleActivity)
          Activities.create(sampleActivity2)

        }
      }
    }

    "find by student and activity" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val person = People.insertPerson(PersonHelpers.person)
          val sampleActivity = ActivityHelpers.sampleActivity.copy(creator = person.id.get)

          val activity = Activities.create(sampleActivity)

          val attempt = Attempts.create(Attempt(None, activity.id.get, person.id.get, currentTime, 5L))
          val found = Attempts.findByStudentAndActivity(person.id.get, activity.id.get)
          found must not be empty
        }
      }
    }

    "find an attempt" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>

          val person = People.insertPerson(PersonHelpers.person)
          val sampleActivity = ActivityHelpers.sampleActivity.copy(creator = person.id.get)

          val activity = Activities.create(sampleActivity)
          activity.id must not be empty

        }
      }
    }
  }
}
