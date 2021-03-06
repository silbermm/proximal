package models

import org.scalatestplus.play._
import play.api.Play.current
import play.api.db.slick.DB
import play.api.mvc._
import play.api.test.Helpers._
import play.api.test._
//import helpers._
import proximaltest.helpers._

class QuestionsSpec extends PlaySpec with Results {

  import proximaltest.helpers.QuestionsHelpers._
  import proximaltest.helpers.StandardsHelpers._

  "Quesitons Model" should {

    "insert a record" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val q = Questions.create(fakeQuestion)
          q.id must not be empty
        }
      }
    }

    "find all statements with a resource" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>

          // create a resource
          val person = People.insertPerson(PersonHelpers.person)
          val resource = ResourceHelpers.sampleResource.copy(creator = person.id)
          val created = Resources.create(resource);
          created.id must not be empty

          val q = Questions.create(fakeQuestion.copy(resourceId = created.id))
          q.id must not be empty
          q.resourceId must not be empty

          val q2 = Questions.create(fakeQuestion)
          val q3 = Questions.create(fakeQuestion)

          val questions = Questions.allWithAResourceId
          val allQuestions = Questions.all

          questions must have length 1
          allQuestions must have length 3
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

    "find all questions with resources" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val resource = ResourceHelpers.sampleResource
          val created = Resources.create(resource)

          val question = Questions.create(QuestionsHelpers.sampleQuestion.copy(resourceId = created.id))

          question.resourceId.get mustBe created.id.get

          val list = Questions.allWithResource
          list must have length 1

        }
      }
    }

  }

}
