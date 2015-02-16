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

class SchoolSpec extends PlaySpec with Results {

  import models._

  var fixtures = List(
    new School(None, "Fairview German School", "3333", "Clifton Ave.", "Cincinnati", Some("OH"), Some("Cincinnati Public"), Some(1)),
    new School(None, "Dater", "3456", "Boudinot Ave", "Cincinnati", Some("OH"), Some("Cincinnati Public"), Some(1))
  )

  "School model" should {
    "insert a record" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val sch = Schools.insert(fixtures(0))
          sch.name mustEqual fixtures(0).name
        }
      }
    }

    "find a school by Name City and State" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val schId = Schools.insert(fixtures(0))
          Schools.findByNameCityState(fixtures(0).name, fixtures(0).city, fixtures(0).state.get) match {
            case Some(sch) => sch.id.get mustEqual schId.id.get
            case _ => fail("unable to find the school")
          }
        }
      }
    }

    "find a list of schools in Cincinnati" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val sch1 = Schools.insert(fixtures(0))
          val sch2 = Schools.insert(fixtures(1))
          Schools.findByCity("Cincinnati") match {
            case l: List[School] => {
              l must contain(sch1)
              l must contain(sch2)
            }
            case _ => fail("Did not get a list back")
          }
        }
      }
    }

    "update a school" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val name2 = "Fairview"
          val sch1 = Schools.insert(fixtures(0))
          val sch2 = sch1.copy(name = name2)
          Schools.update(sch1.id.get, sch2)
          Schools.findById(sch1.id.get) match {
            case Some(school) => school.name mustEqual sch2.name
            case _ => fail("record did not update correctly")
          }
        }
      }
    }

  }

}
