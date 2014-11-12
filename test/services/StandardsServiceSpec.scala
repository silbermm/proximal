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

  val standardsService: StandardsServiceTrait = new StandardsService()
  
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

    "create a standard with education level" in { 
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())){
        standardsService.create(StandardsHelpers.fakeStandard, StandardsHelpers.fakeEducationLevel) match {
          case Some(stan) => { 
            standardsService.findWithEducationLevels(stan.id.get) match {
              case (Some(stan), edLevels) => edLevels must not be empty
              case _ => fail("did not create or retrieve the standard correctly")
            }
          }
          case _ => fail("did not create the standard correctly")
        }
      }
    }

    "create a standard with a list of education levels" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())){
        standardsService.create(StandardsHelpers.fakeStandard, StandardsHelpers.fakeEducationLevels.toList) match {
          case Some(stan) => {
            standardsService.findWithEducationLevels(stan.id.get) match {
              case (Some(stan), edLevels) => edLevels must not be empty
              case _ => fail("did not create or retrieve the standard correctly")
            }
          }
          case _ => fail("unable to create the standard with a list of ed Levels") 
        } 
      }
    }

    "create a list of statements" in { 
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())){
        val numInserted = standardsService.create(StandardsHelpers.fakeStatements)
        numInserted.get mustEqual StandardsHelpers.fakeStatements.length
      }
    }

    "update a statement" in { 
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())){
        val numInserted = standardsService.create(StandardsHelpers.fakeStatement)
        numInserted.id must not be empty
      }
    }

    "find a standard with statements" in { 
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())){
        val standard = standardsService.create(StandardsHelpers.fakeStandard)
        val statement = standardsService.create(StandardsHelpers.fakeStatement.copy(standardId=standard.id))
        statement.standardId.get mustEqual standard.id.get
        standardsService.findWithStatements(standard.id.get) match {
          case (Some(stan), statements) => {
            statements must have length 1
            statements(0).id.get mustEqual statement.id.get
          }
          case _ => fail("failed to retrieve the statements for the standard")
        }
      }
    }

    "find a standard with statements and education levels" in { 
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())){
        standardsService.create(StandardsHelpers.fakeStandard, StandardsHelpers.fakeEducationLevels.toList) match {
          case Some(standard) => {
            val statement = standardsService.create(StandardsHelpers.fakeStatement.copy(standardId=standard.id))
            standardsService.findWithStatementsAndLevels(standard.id.get) match {
              case (Some(stan), statements,levels) => {
                statements must have length 1
                levels must have length StandardsHelpers.fakeEducationLevels.length
              }
              case _ => fail("unable to query for levels and statements...")
            }
          }
          case _ => fail("unable to create the standard with education levels")

        }
      }
    }

  }
}
