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

  "ActivityService" should {

    "respond to creating a new homework activity" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        implicit val actorSystem = ActorSystem("testActorSystem", ConfigFactory.load())
        val actorRef = TestActorRef(new ActivityActor)
        ask(actorRef, CreateHomeworkActivity(List.empty, ActivityHelpers.activityGen.sample.get, ActivityHelpers.homeworkGen.sample.get, List.empty)).mapTo[Option[CreateHomeworkActivity]] map { x =>
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

    "handle acts appropriatly" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val createdAct = DB.withSession { implicit s => Acts.create(ActHelpers.sampleAct) }
        val actsList = List(ActHelpers.sampleAct, ActHelpers.sampleAct, createdAct)
        val newlist = ActivityActor.handleActs(actsList)
        newlist must not be empty
        newlist must have length 3
      }
    }

    "list homework" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val fakeStudent = DB.withSession { implicit s => People.insertPerson(PersonHelpers.person) }
        val fakeActivity = DB.withSession { implicit s => Activities.create(ActivityHelpers.sampleActivity) }
        val fakeHomework = DB.withSession { implicit s => Homeworks.create(ActivityHelpers.sampleHomework.copy(activityId = fakeActivity.id, studentId = fakeStudent.id)) }
        val homeworkList = ActivityActor.listHomework(fakeStudent.id.get)
        homeworkList must have length 1
        homeworkList(0).acts must have length 0
      }
    }

    "create activity acts" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

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