package services

import org.scalatestplus.play._
import play.api.mvc._
import play.api.test.Helpers._
import play.api.test._

class PeopleServiceSpec extends PlaySpec with Results {

  import models._

  var personService = new PersonService()

  var fakePerson = new Person(None, "Matt", Some("Silbernagel"), None, None, Some(1998))

  "Person Service" should {
    "insert and retrieve a record by id" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val p = new Person(None, "Matthew", Some("Silbernagel"), None, None, None)
        personService.createPerson(p)
        personService.findPerson(1L) match {
          case Some(per) => per.firstName mustEqual "Matthew"
          case None => fail("person not found")
        }
      }
    }

    "add a child to a parent" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val parent = personService.createPerson(new Person(None, "Matt", Some("Silbernagel"), None, None, None))
        val child = personService.createPerson(new Person(None, "miles", Some("silbernagel"), None, None, None))
        val relationship = personService.addChild(child, parent);
        relationship.personId mustEqual parent.id.get
        relationship.otherPersonId mustEqual child.id.get
      }
    }

    "find children by parent" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val parent = personService.createPerson(new Person(None, "Matt", Some("Silbernagel"), None, None, None))
        val child = personService.createPerson(new Person(None, "miles", Some("silbernagel"), None, None, None))
        val relationship = personService.addChild(child, parent);
        val listOfChildren = personService.findChildren(parent.id.get)
        listOfChildren.size mustEqual 1
      }
    }

    "add the admin role to user" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val admin = personService.createPerson(fakePerson)
        admin.id must not be empty
        val addAdmin = personService.addAdminRole(admin)
        addAdmin mustEqual 1
        personService.isAdmin(admin.uid.get) mustEqual true
      }
    }
  }
}
