package proximaltest.services

import services._

import play.api.db.slick.DB
import play.api.Play.current

import collection.mutable.Stack
import scala.concurrent.Future
import org.scalatest._
import org.scalatestplus.play._

import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._
import play.api.db.slick.DB
import play.api.Play.current

import scala.compat.Platform

import play.api.libs.json._

import play.api.Logger
import proximaltest.helpers._

import akka.testkit.TestActorRef
import scala.concurrent.duration._
import scala.concurrent.Await
import akka.pattern.ask

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import scala.concurrent.ExecutionContext.Implicits.global
import akka.pattern.ask

class ActivityServiceSpec extends PlaySpec with Results {

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

  "ActivityService" should {
    "respond to creating a new homework activity" in {
      running(SecureSocialHelper.app) {
        implicit val actorSystem = ActorSystem("testActorSystem", ConfigFactory.load())
        val actorRef = TestActorRef(new ActivityActor)
        ask(actorRef, CreateHomeworkActivity(List.empty, ActivityHelpers.sampleActivity.copy(creator = setupUser), ActivityHelpers.homeworkGen.sample.get, List.empty)).mapTo[Option[CreateHomeworkActivity]] map { x =>
          x match {
            case Some(cha) => {
              cha.homework.id must not be empty
              cha.activity.id must not be empty
            }
            case None => fail("nothing was returned")
          }
        }
      }
    }

    "respond to creating a new activity" in {
      running(SecureSocialHelper.app) {
        implicit val actorSystem = ActorSystem("testActorSystem", ConfigFactory.load())
        val actorRef = TestActorRef(new ActivityActor)
        ask(actorRef, CreateActivity(ActivityHelpers.sampleActivity.copy(creator = setupUser), List.empty)).mapTo[Option[Activity]] map { x =>
          x must not be empty
        }
      }
    }

    "respond with exception when creating a new activity and bad list" in {
      running(SecureSocialHelper.app) {
        implicit val actorSystem = ActorSystem("testActorSystem", ConfigFactory.load())
        val actorRef = TestActorRef(new ActivityActor)
        try {
          ask(actorRef, CreateActivity(ActivityHelpers.sampleActivity.copy(creator = setupUser), List(4L, 5L)))
        } catch {
          case e: Throwable => e mustBe a[java.sql.SQLException]
        }
      }
    }

    "handle acts appropriatly" in {
      running(SecureSocialHelper.app) {
        val createdAct = DB.withSession { implicit s => Acts.create(ActHelpers.sampleAct) }
        val actsList = List(ActHelpers.sampleAct, ActHelpers.sampleAct, createdAct)
        val newlist = ActivityActor.handleActs(actsList)
        newlist must not be empty
        newlist must have length 3
      }
    }

    "list homework" in {
      running(SecureSocialHelper.app) {
        val fakeStudent = DB.withSession { implicit s => People.insertPerson(PersonHelpers.person) }
        val fakeActivity = DB.withSession { implicit s => Activities.create(ActivityHelpers.sampleActivity.copy(creator = setupUser)) }
        val fakeHomework = DB.withSession { implicit s => Homeworks.create(ActivityHelpers.sampleHomework.copy(activityId = fakeActivity.id, studentId = fakeStudent.id)) }
        val homeworkList = ActivityActor.listHomework(fakeStudent.id.get)
        homeworkList must have length 1
        homeworkList(0).acts must have length 0
      }
    }

    "create activity" in {
      running(SecureSocialHelper.app) {
        val fakeActivity = CreateActivity(ActivityHelpers.sampleActivity.copy(creator = setupUser), List.empty)
        val created = ActivityActor.createActivity(fakeActivity)
        created must not be empty
      }
    }

    "throw an exception when attempting to create activity" in {
      running(SecureSocialHelper.app) {
        val fakeActivity = CreateActivity(ActivityHelpers.sampleActivity.copy(creator = setupUser), List(4L, 5L, 6L))
        an[org.h2.jdbc.JdbcSQLException] must be thrownBy ActivityActor.createActivity(fakeActivity)
      }
    }

    "create activity statements" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

      }
    }

    "create Homework" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

      }
    }

  }
}
