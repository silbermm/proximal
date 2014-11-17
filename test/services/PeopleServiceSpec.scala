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

class PeopleServiceSpec extends PlaySpec with Results {

  import models._

  
  var personService = new PersonService() 

  "Person Service" should {
    "insert and retrieve a record by id" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())){
        val p = new Person(None, "Matthew", Some("Silbernagel"), None,None, None)
        personService.createPerson(p)
        personService.findPerson(1L) match {
          case Some(per) => per.firstName mustEqual "Matthew"
          case None => fail("person not found")
        }
      }
    }
    
    "add a child to a parent" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())){
        val parent = personService.createPerson(new Person(None, "Matt", Some("Silbernagel"), None,None, None))
        val child = personService.createPerson(new Person(None, "miles", Some("silbernagel"), None, None, None))
        val relationship = personService.addChild(child,parent);
        relationship.personId mustEqual parent.id.get
        relationship.otherPersonId mustEqual child.id.get
      }
    }

    "find children by parent" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())){
        val parent = personService.createPerson(new Person(None, "Matt", Some("Silbernagel"), None, None,None))
        val child = personService.createPerson(new Person(None, "miles", Some("silbernagel"), None, None, None))
        val relationship = personService.addChild(child,parent); 
        val listOfChildren = personService.findChildren(parent.id.get)
        listOfChildren.size mustEqual 1
      }
    }
  }
}
