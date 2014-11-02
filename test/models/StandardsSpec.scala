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

class StandardsSpec extends PlaySpec with Results {

  import models._

  "Standards model" should {
    "insert a record and find it" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())){
        DB.withSession{ implicit s=>
          val standard = Standards.insert(StandardsHelpers.fakeStandard)
          Standards.find(standard.id.get) match {
            case Some(st) => st.title mustEqual StandardsHelpers.fakeStandard.title
            case None => fail("The recored was not added properly")
          }
        }
      }
    }

    "find a standard by name" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())){
        DB.withSession{ implicit s=>
          val standard = Standards.insert(StandardsHelpers.fakeStandard)
          Standards.find(standard.title) match {
            case st: List[Standard] => {
              st must not be empty 
              st must contain (standard)
            }
            case _ => fail("The recored was not added properly")
          }

        }
      }
    }

    "delete a standard" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())){
        DB.withSession{ implicit s=>
          val standard = Standards.insert(StandardsHelpers.fakeStandard)
          Standards.delete(standard)
          val st = Standards.find(standard.id.get)
          st mustBe empty
        }
      }
    }

    "update a standard" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())){
        DB.withSession{ implicit s=>
          val standard = Standards.insert(StandardsHelpers.fakeStandard)
          val updated = standard.copy(title = "new title")
          val up = Standards.update(standard.id.get, updated)
          up mustBe 1

          Standards.find(standard.id.get) match {
            case Some(st) => st.title mustBe "new title"
            case _ => fail("update failed")
          }
        
        }
      }
    }
    
    "list all standards" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())){
        DB.withSession{ implicit s=>
          val standard = Standards.insert(StandardsHelpers.fakeStandard)
          Standards.list match {
            case st : List[Standard] => st must have length 1
            case _ => fail("didn't recieve a list back")
          }
        }
      }
    }

    "get Education Levels that the standard applys to" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())){
        DB.withSession{ implicit s=>
          val e = EducationLevels.insert(StandardsHelpers.fakeEducationLevel)
          e.id must not be empty

          val standard = Standards.insert(StandardsHelpers.fakeStandard)
          standard.id must not be empty

          val standardLevel = StandardLevels.insert(new StandardLevel(None,e.id.get,standard.id.get))
          standardLevel.id must not be empty
         
          val exists = Standards.findWithEducationLevels(standard.id.get)
          exists._1 must not be empty
          exists._2 must not be empty
              
        }
      }
    }
  }

}

