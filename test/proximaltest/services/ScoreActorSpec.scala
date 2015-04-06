package proximaltest.services

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.testkit.TestActorRef
import com.typesafe.config.ConfigFactory
import org.scalatestplus.play._
import play.api.mvc._
import play.api.test.Helpers._
import play.api.test._
import proximaltest.helpers._
import services._

import scala.concurrent.ExecutionContext.Implicits.global

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
