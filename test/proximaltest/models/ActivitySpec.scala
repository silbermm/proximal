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
//import helpers._
import proximaltest.helpers._

class ActivitySpec extends PlaySpec with Results {
  import models._

  "Activity model" should {

    "create an activity" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val sampleActivity = ActivityHelpers.sampleActivity
          val activity = Activities.create(sampleActivity)
          activity.id must not be empty
        }
      }
    }

    "find an activity" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val sampleActivity = ActivityHelpers.sampleActivity
          val activity = Activities.create(sampleActivity)
          activity.id must not be empty
          Activities.find(activity.id.get) match {
            case Some(a) => a.id.get mustEqual activity.id.get
            case None => fail("unable to find the activity in the database :(")
          }
        }
      }
    }

    "list all activity" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          Activities.create(ActivityHelpers.sampleActivity)
          Activities.create(ActivityHelpers.sampleActivity)
          Activities.all must not be empty
          Activities.all must have length 2
        }
      }
    }

    "delete an activity" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val sampleActivity = ActivityHelpers.sampleActivity
          val activity = Activities.create(sampleActivity)
          activity.id must not be empty
          val deleted = Activities.delete(activity)
          deleted mustBe 1
        }
      }
    }

    "find and activity with Statements" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          // first we need a standard and statement
          val standard = Standards.insert(StandardsHelpers.fakeStandard)
          standard.id must not be empty
          val statement1Rec = StandardsHelpers.fakeStatement.copy(standardId = standard.id)
          val statement2Rec = StandardsHelpers.fakeStatement2.copy(standardId = standard.id)
          val statement1 = Statements.insert(statement1Rec)
          val statement2 = Statements.insert(statement2Rec)
          Statements.list must have length 2

          // Now create an activity and associate the above statements
          val activity = Activities.create(ActivityHelpers.sampleActivity)
          val activityStatement1 = ActivityStatements.create(ActivityStatement(None, activity.id.get, statement1.id.get))
          val activityStatement2 = ActivityStatements.create(ActivityStatement(None, activity.id.get, statement2.id.get))
          ActivityStatements.all must have length 2

          val activityList = Activities.findWithStatements(activity.id.get)
          activityList must not be empty
          activityList.get.statements must have length 2

        }
      }
    }
    "find and activity with Acts" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val activity = Activities.create(ActivityHelpers.sampleActivity)
          val act = Acts.create(ActHelpers.sampleAct)
          val activityActs = ActivityActs.create(ActivityAct(None, activity.id.get, act.id.get))
          activityActs.id must not be empty

          val activityWithActs = Activities.findWithActs(activity.id.get)
          activityWithActs must not be empty
          activityWithActs.get.acts must have length 1
        }
      }
    }

    "find an activity with statements and acts" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          // first we need a standard and statement
          val standard = Standards.insert(StandardsHelpers.fakeStandard)
          standard.id must not be empty
          val statement1Rec = StandardsHelpers.fakeStatement.copy(standardId = standard.id)
          val statement2Rec = StandardsHelpers.fakeStatement2.copy(standardId = standard.id)
          val statement1 = Statements.insert(statement1Rec)
          val statement2 = Statements.insert(statement2Rec)
          Statements.list must have length 2

          // Now create an activity and associate the above statements
          val activity = Activities.create(ActivityHelpers.sampleActivity)
          val activityStatement1 = ActivityStatements.create(ActivityStatement(None, activity.id.get, statement1.id.get))
          val activityStatement2 = ActivityStatements.create(ActivityStatement(None, activity.id.get, statement2.id.get))
          ActivityStatements.all must have length 2

          // Create the act and associate the activity
          val act = Acts.create(ActHelpers.sampleAct)
          val activityActs = ActivityActs.create(ActivityAct(None, activity.id.get, act.id.get))
          activityActs.id must not be empty

          val complexActivity = Activities.findWithStatementsAndActs(activity.id.get)
          complexActivity must not be empty
          complexActivity.get.acts must have length 1
          complexActivity.get.statements must have length 2
        }
      }
    }
  }
}