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

class DummySpec extends PlaySpec with Results {
  import models._

  "Dummy model" should {
    
    "insert a dummy" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())){
        DB.withSession{ implicit s=>
          val d = new Dummy(None, "Dummy title", "Dummy Description")
          val inserted = Dummies.insert(d) 
          inserted.id must not be empty
          inserted.name mustEqual "Dummy title"
        }
      }
    }
    
  }


}
