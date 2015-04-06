package proximaltest.models

import org.scalatestplus.play._
import play.api.Play.current
import play.api.db.slick.DB
import play.api.mvc._
import play.api.test.Helpers._
import play.api.test._
//import helpers._
import proximaltest.helpers._

class ScoreSpec extends PlaySpec with Results {
  import models._
  import proximaltest.helpers.QuestionsHelpers._

  import scala.compat.Platform

  val fakePerson = new Person(None, "Matthew", Some("Silbernagel"), None, None, None)

  "Scores model" should {
    "insert a score" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val person = People.insertPerson(PersonHelpers.person)
          val question = Questions.create(fakeQuestion)
          val t = Platform.currentTime

          person.id must not be empty
          question.id must not be empty
          val score = Score(None, person.id.get, Some(question.id.get), None, None, Some(5), t)
          val createdScore = Scores.create(score);
          createdScore.id must not be empty
          createdScore.timestamp mustBe t
        }
      }
    }

    "should find a score by activity and student" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val person = People.insertPerson(PersonHelpers.person)
          val child = People.insertPerson(PersonHelpers.person)
          val activity = Activities.create(ActivityHelpers.sampleActivity.copy(creator = person.id.get))
          val t = Platform.currentTime
          person.id must not be empty
          activity.id must not be empty
          val score = Score(None, child.id.get, None, None, activity.id, Some(5), t)
          val createdScore = Scores.create(score)
          createdScore.id must not be empty

          val found = Scores.findByActivityAndStudent(activity.id.get, child.id.get)
          found must not be empty
        }
      }
    }

    "delete a score" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val person = People.insertPerson(PersonHelpers.person)
          val question = Questions.create(fakeQuestion)
          val t = Platform.currentTime
          person.id must not be empty
          question.id must not be empty
          val score = Score(None, person.id.get, Some(question.id.get), None, None, Some(5), t)
          val createdScore = Scores.create(score);
          createdScore.id must not be empty

          val deleted = Scores.delete(createdScore.id.get)
          deleted mustBe 1

        }
      }
    }
  }
}
