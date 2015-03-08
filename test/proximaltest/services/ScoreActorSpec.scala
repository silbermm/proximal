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

class ScoreActorSpec extends PlaySpec with Results {

  import models._

  "Score Actor" should {

    "respond to listing scores" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        implicit val actorSystem = ActorSystem("testActorSystem", ConfigFactory.load())
        val actorRef = TestActorRef(new ScoreActor)
        ask(actorRef, ListScores(90L)).mapTo[List[Score]] map { x =>
          x mustBe a[List[Score]]
        }
      }
    }

    "respond to an update request" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val fakeScore = ScoreHelpers.sampleScore
        implicit val actorSystem = ActorSystem("testActorSystem", ConfigFactory.load())
        val actorRef = TestActorRef(new ScoreActor)
        ask(actorRef, UpdateScore(fakeScore)).mapTo[Int] map { x =>
          x mustBe a[Int]
          x mustEqual 0
        }
      }

    }
  }
}
