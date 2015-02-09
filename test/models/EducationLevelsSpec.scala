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

class EducationLevelsSpec extends PlaySpec with Results {

  import models._

  "Education Levels" should {

    "insert a single level and find it by id" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val e = EducationLevels.insert(StandardsHelpers.fakeEducationLevel)
          e.id must not be empty
          EducationLevels.find(e.id.get).map(level =>
            level.description mustEqual e.description
          ).getOrElse(
            fail("Did not find the level after insertion")
          )
        }
      }
    }

    "insert a Seq" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val numberInserted = EducationLevels.insert(StandardsHelpers.fakeEducationLevels)
          numberInserted.map(i => {
            Logger.debug(i.toString() + " mustEqual " + StandardsHelpers.fakeEducationLevels.length.toString())
            i mustEqual StandardsHelpers.fakeEducationLevels.length
          }
          ).getOrElse(
            fail("did anything get inserted?")
          )
        }
      }
    }

    "list all levels" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val emptyList = EducationLevels.list
          emptyList mustBe empty

          EducationLevels.insert(StandardsHelpers.fakeEducationLevels)
          val edList = EducationLevels.list
          edList must not be empty
        }
      }
    }

    "find by description" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          EducationLevels.insert(StandardsHelpers.fakeEducationLevel)
          EducationLevels.find(StandardsHelpers.fakeEducationLevel.description).map(ed =>
            ed.description mustEqual StandardsHelpers.fakeEducationLevel.description
          ).getOrElse(
            fail()
          )
        }
      }
    }

    "not allow two levels with the same description" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val f = EducationLevels.insert(StandardsHelpers.fakeEducationLevel)
          val g = EducationLevels.insert(StandardsHelpers.fakeEducationLevel)
          g.id.get mustEqual f.id.get
        }
      }
    }

    "delete" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val e = EducationLevels.insert(StandardsHelpers.fakeEducationLevel)
          val numDeleted = EducationLevels.delete(e)
          numDeleted mustEqual 1
        }
      }
    }

    "get Standards the are in the education level" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val e = EducationLevels.insert(StandardsHelpers.fakeEducationLevel)
          e.id must not be empty

          val standard = Standards.insert(StandardsHelpers.fakeStandard)
          standard.id must not be empty

          val standardLevel = StandardLevels.insert(new StandardLevel(None, e.id.get, standard.id.get))
          standardLevel.id must not be empty

          val exists = EducationLevels.findWithStandards(e.id.get)
          exists must not be empty

          exists.head._2.id.get mustEqual standard.id.get
          exists.head._1.id.get mustEqual e.id.get

        }
      }
    }

    "get Statements that are in the education level" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val e = EducationLevels.insert(StandardsHelpers.fakeEducationLevel)
          e.id must not be empty

          val statement = Statements.insert(StandardsHelpers.fakeStatement)
          statement.id must not be empty

          val statementLevel = StatementLevels.insert(new StatementLevel(None, e.id.get, statement.id.get))
          statementLevel.id must not be empty

          val exists = EducationLevels.findWithStatements(e.id.get)
          exists must not be empty

          exists.head._2.id.get mustEqual statement.id.get
          exists.head._1.id.get mustEqual e.id.get

        }
      }
    }

  }

}
