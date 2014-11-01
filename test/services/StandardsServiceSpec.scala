package services

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

class StandardsServiceSpec extends PlaySpec with Results {

  import models._

  var standardsService = new StandardsService()
  
  "Standards Service" should {
    "create a standard" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())){
      
      }
    }

    "update a standard" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())){
      
      }
    }

    "find a standard by id" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())){
      
      }
    }
    
    "list the standards" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())){
      
      }
    }

    "delete a standard" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())){
      
      }
    }

  }
}
