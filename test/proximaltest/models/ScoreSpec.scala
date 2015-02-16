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
//import helpers._
import proximaltest.helpers._

class ScoreSpec extends PlaySpec with Results {
  import models._
  import QuestionsHelpers._
  import java.sql.Timestamp
  import scala.compat.Platform

  val fakePerson = new Person(None, "Matthew", Some("Silbernagel"), None, None, None)

  "Scores model" should {
    "insert a score" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val person = People.insertPerson(fakePerson)
          val question = Questions.create(fakeQuestion)
          val t = Platform.currentTime

          person.id must not be empty
          question.id must not be empty
          val score = Score(None, person.id.get, question.id.get, 5, t)
          val createdScore = Scores.insert(score);
          createdScore.id must not be empty
          createdScore.timestamp mustBe t
        }
      }
    }
  }
}
