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

class QuestionsSpec extends PlaySpec with Results {

  import models._
  import QuestionsHelpers._
  import StandardsHelpers._

  "Quesitons Model" should {

    "insert a record" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val q = Questions.create(fakeQuestion)
          q.id must not be empty
        }
      }
    }

    "have a statement" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val q = Questions.create(fakeQuestion)
          q.id must not be empty

          val standard = Standards.insert(fakeStandard)
          val statement = Statements.insert(fakeStatement.copy(standardId = standard.id))

          val questionWithStatemnents = new QuestionWithStatements(None, q.id.get, statement.id.get)
          val qs = QuestionsWithStatements.create(questionWithStatemnents)
          qs.id must not be empty
        }
      }
    }

    "find a list of questions_statements" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val q = Questions.create(fakeQuestion)
          q.id must not be empty

          val standard = Standards.insert(fakeStandard)
          val statement = Statements.insert(fakeStatement.copy(standardId = standard.id))

          val questionWithStatemnents = new QuestionWithStatements(None, q.id.get, statement.id.get)
          val qs = QuestionsWithStatements.create(questionWithStatemnents)
          qs.id must not be empty

          QuestionsWithStatements.findByQuestionId(q.id.get) must have length 1
        }
      }
    }

    "find a question and related statements" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val q = Questions.create(fakeQuestion)
          q.id must not be empty

          val standard = Standards.insert(fakeStandard)
          val statement = Statements.insert(fakeStatement.copy(standardId = standard.id))

          val questionWithStatemnents = new QuestionWithStatements(None, q.id.get, statement.id.get)
          val qs = QuestionsWithStatements.create(questionWithStatemnents)
          qs.id must not be empty

          val questionsWithStatements = Questions.findWithStatements(qs.id.get)
          questionsWithStatements.statements.get must have length 1

        }
      }
    }

    "find a list of questions when given a statement" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val q = Questions.create(fakeQuestion)
          val q2 = Questions.create(fakeQuestion.copy(text = "anotherQuestion"))
          q.id must not be empty

          val standard = Standards.insert(fakeStandard)
          val statement = Statements.insert(fakeStatement.copy(standardId = standard.id))

          val questionWithStatemnents = new QuestionWithStatements(None, q.id.get, statement.id.get)
          val qs = QuestionsWithStatements.create(new QuestionWithStatements(None, q.id.get, statement.id.get))
          val qs2 = QuestionsWithStatements.create(new QuestionWithStatements(None, q2.id.get, statement.id.get))

          qs.id must not be empty
          qs2.id must not be empty

          Questions.findByStatement(statement.id.get) match {
            case (questions, Some(statement)) => questions must have length 2
            case _ => fail("did not get the correct data")
          }
        }
      }
    }

  }

}
