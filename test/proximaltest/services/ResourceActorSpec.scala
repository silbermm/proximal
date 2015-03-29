package proximaltest.services

import services._

import play.api.db.slick.DB
import play.api.Play.current

import collection.mutable.Stack
import scala.concurrent.Future
import org.scalatestplus.play._

import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._

import scala.compat.Platform

import play.api.libs.json._

import play.api.Logger
import proximaltest.helpers._

import akka.testkit.TestActorRef
import scala.concurrent.Await
import akka.pattern.ask

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import scala.concurrent.ExecutionContext.Implicits.global
import akka.pattern.ask

import scala.util.{ Try, Success, Failure }

class ResourceActorSpec extends PlaySpec with Results {
  import models._

  def setupUser: Long = {
    val creds1 = cookies(route(FakeRequest(POST, "/authenticate/naive").withTextBody("user")).get)
    val Some(user) = route(FakeRequest(GET, "/api/v1/profile").withCookies(creds1.get("id").get))
    val json: JsValue = Json.parse(contentAsString(user))
    contentType(user) mustEqual Some("application/json")
    val uid = (json \ "user" \ "uid").as[Long]
    DB.withSession { implicit s => People.insertPerson(PersonHelpers.person.copy(uid = Some(uid))) }
    uid
  }

  "Resource Actor" should {

    "respond to creating a new resource" in {
      running(SecureSocialHelper.app) {
        implicit val actorSystem = ActorSystem("testActorSystem", ConfigFactory.load())
        val actorRef = TestActorRef(new ResourceActor)
        val sampleresource = ResourceHelpers.sampleResource
        ask(actorRef, CreateResource(sampleresource, None)).value map { m =>
          m match {
            case Success(res: Resource) => res.id must not be empty
            case Success(res: Any) => fail("expected to get a resource")
            case Failure(_) => fail("nothing came back")
          }
        } getOrElse { fail("map doesn't work") }
      }
    }

    "respond to deleting a resource" in {
      running(SecureSocialHelper.app) {
        implicit val actorSystem = ActorSystem("testActorSystem", ConfigFactory.load())
        val actorRef = TestActorRef(new ResourceActor)
        val sampleresource = ResourceHelpers.sampleResource
        val resource = ResourceActor.createResource(ResourceHelpers.sampleResource, None)

        val future = ask(actorRef, DeleteResource(resource))
        future.value.get match {
          case Success(del: Int) => del mustBe 1
          case Success(somethingElse: Any) => fail("should have received an integer")
          case Failure(e) => fail(s"Something went wrong $e")
        }
      }
    }

    "respond to updating a resource" in {
      running(SecureSocialHelper.app) {
        implicit val actorSystem = ActorSystem("testActorSystem", ConfigFactory.load())
        val actorRef = TestActorRef(new ResourceActor)
        val sampleresource = ResourceHelpers.sampleResource

        val resource = ResourceActor.createResource(ResourceHelpers.sampleResource, None)
        val future = ask(actorRef, UpdateResource(resource.copy(title = Some("something"))))
        future.value map (message =>
          message mustBe a[Success[Int]]
        )
      }
    }

    "respond to searching for a resource" in {
      running(SecureSocialHelper.app) {
        implicit val actorSystem = ActorSystem("testActorSystem", ConfigFactory.load())
        val actorRef = TestActorRef(new ResourceActor)
        val sampleresource = ResourceHelpers.sampleResource

        val resource = ResourceActor.createResource(ResourceHelpers.sampleResource, None)
        val future = ask(actorRef, GetResource(resource.id.get))
        future.value map (message =>
          message mustBe a[Success[Option[Resource]]]
        )
      }
    }
  }

}

