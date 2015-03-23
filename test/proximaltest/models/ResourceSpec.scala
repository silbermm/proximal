package proximaltest.models

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

class ResourceSpec extends PlaySpec with Results {
  import models._

  "Resource Model" should {

    "create a resource" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val person = People.insertPerson(PersonHelpers.person)
          val resource = ResourceHelpers.sampleResource.copy(creator = person.id)
          val created = Resources.create(resource);
          created.id must not be empty
        }
      }
    }

    "delete a resource" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val person = People.insertPerson(PersonHelpers.person)
          val resource = ResourceHelpers.sampleResource.copy(creator = person.id)
          val created = Resources.create(resource)
          created.id must not be empty
          val deleted = Resources.delete(created)
          deleted mustBe 1
        }
      }
    }

    "find a resource" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val person = People.insertPerson(PersonHelpers.person)
          val resource = ResourceHelpers.sampleResource.copy(creator = person.id)
          val created = Resources.create(resource)
          created.id must not be empty

          Resources.find(created.id.get) match {
            case Some(r) => r.title mustEqual created.title
            case None => fail("did not find an entity")
          }
        }
      }
    }

    "update a resource" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val person = People.insertPerson(PersonHelpers.person)
          val resource = ResourceHelpers.sampleResource.copy(creator = person.id)
          val created = Resources.create(resource)
          created.id must not be empty

          val updated = Resources.update(created.copy(title = "some nonsense"))
          Resources.find(created.id.get) match {
            case Some(r) => r.title mustEqual "some nonsense"
            case None => fail("did not find an entity")
          }
        }
      }
    }

    "list all resources" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val person = People.insertPerson(PersonHelpers.person)
          val resource = ResourceHelpers.sampleResource.copy(creator = person.id)
          val created = Resources.create(resource)
          created.id must not be empty

          val resource2 = ResourceHelpers.sampleResource.copy(creator = person.id)
          val created2 = Resources.create(resource2)
          created2.id must not be empty

          val all = Resources.all
          all must not be empty
          all must have length 2
        }
      }
    }

    //"list all resources with questions" in {
    //running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
    //DB.withSession { implicit s =>
    //val resource = ResourceHelpers.sampleResource
    //val created = Resources.create(resource)
    //created.id must not be empty

    //val resource2 = ResourceHelpers.sampleResource.copy(category = Some("question"))
    //val created2 = Resources.create(resource2)
    //created2.id must not be empty
    //val sampleQuestion = QuestionsHelpers.sampleQuestion.copy(resourceId = resource2.id)
    //val question = Questions.create(sampleQuestion)

    //val questions = Resources.allQuestions

    //questions mustBe a[List[ResourceWithQuestion]]
    //questions must have length 1
    //questions(0).question.get.id.get mustBe question.id.get

    //}
    //}
    /*}*/

  }
}
