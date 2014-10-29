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

class PeopleSpec extends PlaySpec with Results {

  import models._

  val fakePerson = new Person(None, "Matthew", Some("Silbernagel"), None, None)
  val fakeSchool = new School(None,
                              "Fairview German School",
                              "999", 
                              "Clifton", 
                              "Cincinnati", 
                              Some("OH"), 
                              Some("Cincy Public"),  
                              Some(1)
                              )

  "Person model" should {
    "insert and retrieve a record by id" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())){
        DB.withSession{ implicit s=>
          val person = People.insertPerson(fakePerson)
          People.findPerson(person.id.get) match {
            case Some(per) => per.firstName mustEqual "Matthew"
            case None => fail("person not found")
          }
        }
      }
    }

    "update a person" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())){
        DB.withSession{ implicit s=>
          val person = People.insertPerson(fakePerson);
          val updated = person.copy(lastName = Some("Smith"))
          People.updatePerson(person.id.get, updated) match {
            case Some(per) => per.lastName.get mustEqual "Smith"
            case _ => fail("failed to update person record")
          }
        }
      }
    }

    "delete a person" in {
       running(FakeApplication(additionalConfiguration = inMemoryDatabase())){
        DB.withSession{ implicit s=>
          val person = People.insertPerson(fakePerson);
          People.deletePerson(person.id.get)
          val res = People.findPerson(person.id.get) 
          res mustBe empty
        }
       }
    }

    "add a school to a child" in {
      import java.util.Date
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())){
        DB.withSession{ implicit s=>
          val child = People.insertPerson(fakePerson)
          val school = Schools.insert(fakeSchool)
          
          val a =  People.addSchoolToChild(child,school,Some(new Date()), None, Some(1))
          a mustBe 1

        }
      }
    }

    "find a list of schools for a child" in {
      import java.util.Date
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())){
        DB.withSession{ implicit s=>
          val child = People.insertPerson(fakePerson)
          val school = Schools.insert(fakeSchool)
         
          Logger.debug(school.toString)

          val a =  People.addSchoolToChild(child,school,Some(new Date()), None, Some(1))
          a mustBe 1
          
          val schools = People.findSchoolsForChild(child.id.get)
          schools must not be empty
          schools must contain (school)

        }
      }
    }



  }

  "Relationship model" should {
    "add a child to a parent" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())){
        DB.withSession{ implicit s=>
          val parent = People.insertPerson(new Person(None, "Matt", Some("Silbernagel"), None, None))
          val child = People.insertPerson(new Person(None, "miles", Some("silbernagel"), None, None))
          val relationship = People.addChild(child,parent);
          relationship.personId mustEqual parent.id.get
          relationship.otherPersonId mustEqual child.id.get
        }
      }
    }

    "find children by parent" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())){
        DB.withSession{ implicit s=>
          val parent = People.insertPerson(new Person(None, "Matt", Some("Silbernagel"), None, None))
          val child = People.insertPerson(new Person(None, "miles", Some("silbernagel"), None, None))
          val relationship = People.addChild(child,parent); 
          val listOfChildren = People.findChildrenFor(parent.id.get)
          listOfChildren.size mustEqual 1
        }
      }
    }
  }




}
