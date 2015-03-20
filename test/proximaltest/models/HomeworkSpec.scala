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

class HomeworkSpec extends PlaySpec with Results {
  import models._

  "Homework model" should {

    "create homework" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          //we first need an activity

          val person = People.insertPerson(PersonHelpers.person)
          val sampleActivity = ActivityHelpers.sampleActivity.copy(creator = person.id.get)
          val activity = Activities.create(sampleActivity)
          activity.id must not be empty

          val sampleHomework = ActivityHelpers.sampleHomework.copy(activityId = activity.id)
          val homework = Homeworks.create(sampleHomework)
          homework.id must not be empty
        }
      }
    }

    "find a homework" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val sampleHomework = ActivityHelpers.sampleHomework
          val homework = Homeworks.create(sampleHomework)
          homework.id must not be empty

          Homeworks.find(homework.id.get) match {
            case Some(h) => h.id.get mustBe homework.id.get
            case None => fail("could not find the homework")
          }
        }
      }
    }

    "list all homework" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          Homeworks.create(ActivityHelpers.sampleHomework)
          Homeworks.create(ActivityHelpers.sampleHomework)
          Homeworks.create(ActivityHelpers.sampleHomework)
          Homeworks.all must have length 3
        }
      }
    }

    "delete a homework" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val homework = Homeworks.create(ActivityHelpers.sampleHomework)
          val deleted = Homeworks.delete(homework)
          deleted mustBe 1
        }
      }
    }

    "find a homework by studentId" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      }
    }

    "find all homework with the actvitiy and acts by studentid" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        // this is way too much for one test, but here it is anyways 
        DB.withSession { implicit s =>
          // we first need an activity

          val person = People.insertPerson(PersonHelpers.person)
          val sampleActivity = ActivityHelpers.sampleActivity.copy(creator = person.id.get)
          val activity = Activities.create(sampleActivity)
          activity.id must not be empty

          // create acts for the activity
          val act1 = Acts.create(ActHelpers.sampleAct)
          val act2 = Acts.create(ActHelpers.sampleAct)
          act1.id must not be empty
          act2.id must not be empty

          // Tie acts to the activity
          val activityAct1 = ActivityActs.create(ActivityAct(None, activity.id.get, act1.id.get))
          val activityAct2 = ActivityActs.create(ActivityAct(None, activity.id.get, act2.id.get))
          activityAct1.id must not be empty
          activityAct2.id must not be empty

          // Create a fake student
          val child = People.insertPerson(PersonHelpers.person)
          child.id must not be empty

          // create a homework tied to that activity
          val sampleHomework = ActivityHelpers.sampleHomework.copy(activityId = activity.id, studentId = child.id)
          val sampleHomework2 = ActivityHelpers.sampleHomework.copy(activityId = activity.id, studentId = child.id)
          val homework1 = Homeworks.create(sampleHomework)
          val homework2 = Homeworks.create(sampleHomework2)
          homework1.id must not be empty
          homework2.id must not be empty

          val crazyQuery = Homeworks.findByStudentWithActivitiesAndActs(child.id.get);
          crazyQuery must not be empty
          crazyQuery must have length 2
          crazyQuery(0).acts must have length 2

        }
      }

    }
  }
}
