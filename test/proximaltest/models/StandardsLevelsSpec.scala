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
//import helpers.StandardsHelpers._
import proximaltest.helpers.StandardsHelpers._

class StandardsLevelsSpec extends PlaySpec with Results {
  import models._

  "Standards Levels " should {

    "insert a row and find it by id" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          //first we need an education level and standard level 
          val e = EducationLevels.insert(fakeEducationLevel)
          e.id must not be empty

          val standard = Standards.insert(fakeStandard)
          standard.id must not be empty

          val st = new StandardLevel(None, e.id.get, standard.id.get)
          val standardLevel = StandardLevels.insert(st)
          standardLevel.id must not be empty

          StandardLevels.find(standardLevel.id.get).map(level =>
            level.id must not be empty
          ).getOrElse(
            fail("unable to find by id")
          )
        }
      }
    }
  }
}
