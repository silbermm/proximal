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

class AssesmentServiceSpec extends PlaySpec with Results {

  import models._

  var personService = new PersonService()
  var standardsService = new StandardsService()

  "Assesment Service" should {
    "generate the next question" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val e = DB.withSession { implicit s =>
          // Generate a ed level for the student and questions to use
          EducationLevels.insert(StandardsHelpers.fakeEducationLevel)
        }
        // Generate a parent and student first
        val parent = personService.createPerson(PersonHelpers.person)
        val child = personService.createPerson(PersonHelpers.child(e.id.get))
        val relationship = personService.addChild(child, parent);

        // Add a fake standard and statements  
        val statement1 = standardsService.create(StandardsHelpers.fakeStatements(0))
        val statement2 = standardsService.create(StandardsHelpers.fakeStatements(1))

        statement1.id must not be empty
        statement2.id must not be empty

        DB.withSession { implicit s =>
          val e1 = StatementLevels.insert(StatementLevel(None, e.id.get, statement1.id.get))
          val e2 = StatementLevels.insert(StatementLevel(None, e.id.get, statement2.id.get))
          e1.id must not be empty
          e2.id must not be empty

          (1 to 5) foreach (x => {
            val nq = Questions.create(QuestionsHelpers.questionGen.sample.get)
            val qWs = QuestionsWithStatements.create(QuestionWithStatements(None, nq.id.get, e1.id.get))
          })

          (1 to 5) foreach (x => {
            val nq = Questions.create(QuestionsHelpers.questionGen.sample.get)
            val qWs = QuestionsWithStatements.create(QuestionWithStatements(None, nq.id.get, e2.id.get))
          })

          val nxtQ = AssesmentService.nextQuestion(child.id.get)
          nxtQ.id must not be empty
          val qscor = QuestionScores.create(QuestionScore(None, child.id.get, nxtQ.id.get, Some(5), Platform.currentTime))
          qscor.id must not be empty
          val nxtQ2 = AssesmentService.nextQuestion(child.id.get)
          nxtQ.id.get must not be nxtQ2.id.get

        }

      }
    }

  }
}
