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

  "Person model" should {
    "insert and retrieve a record by id" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())){
        DB.withSession{ implicit s=>
          val p = new Person(None, "Matthew", Some("Silbernagel"), None, None)
          People.insertPerson(p)
          People.findPerson(1L) match {
            case Some(per) => per.firstName mustEqual "Matthew"
            case None => fail("person not found")
          }
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
