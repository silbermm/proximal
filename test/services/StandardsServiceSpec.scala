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
    "create a standard and find by id" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())){
        val standard = StandardsHelpers.fakeStandard
        val st = standardsService.create(standard)
        standardsService.find(st.id.get).map( s => 
          st.title mustEqual s.title
        ).getOrElse(
          fail("standard not created")
        )
      }
    }

    "update a standard" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())){
        val standard = StandardsHelpers.fakeStandard
        val st = standardsService.create(standard)
        val stUpdate = st.copy(title="Stupid Title")
        val stUpdated = standardsService.update(st.id.get,stUpdate)
        stUpdated mustEqual 1
      }
    }
 
    "list the standards" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())){
        val listOfStandards = standardsService.list    
        listOfStandards.size  mustEqual 0
        standardsService.create(StandardsHelpers.fakeStandard)
        standardsService.list.size mustEqual 1
      }
    }

    "delete a standard" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())){
        val standard = standardsService.create(StandardsHelpers.fakeStandard)
        val deleted = standardsService.delete(standard)
        deleted mustEqual 1
      }
    }

  }
}
