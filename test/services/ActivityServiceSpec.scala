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

import scala.compat.Platform

import play.api.Logger
import helpers._

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
        ask(actorRef, CreateHomeworkActivity(ActivityHelpers.activityGen.sample.get, ActivityHelpers.homeworkGen.sample.get)).mapTo[Option[CreateHomeworkActivity]] map { x =>
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
  }
}