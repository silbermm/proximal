package services

import play.api.libs.concurrent.Akka
import play.api.Logger
import services._
import models._
import play.api.db.slick.DB
import play.api.Play.current
import scala.util.Random
import scala.compat.Platform
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import play.api.libs.concurrent.Execution.Implicits.defaultContext

case class ChildAndStandard(childId: Long, standardId: Long)
case class AssessmentQuestion(assessment: Assesment, question: JsonQuestion)

class AssessmentActor extends Actor {

  private val random = new Random

  def receive = {
    case childAndStandard: ChildAndStandard => {
      val asses = DB.withSession { implicit s =>
        Assesments.create(Assesment(None, childAndStandard.childId, Platform.currentTime, None))
      }
      sender ! Some(AssessmentQuestion(asses, nextQuestion(childAndStandard.childId, childAndStandard.standardId)))
    }
    case _ => sender ! None
  }

  def nextQuestion(studentId: Long, standardId: Long): JsonQuestion = {
    DB.withSession { implicit s =>
      try {
        // Find the student and his/her grade level 
        val (student: Person, edLevel: EducationLevel) = People.findWithEducationLevel(studentId);

        // Find all statements in students grade level 
        val statements: List[Statement] = StatementLevels.findStatements(edLevel.id.get);

        // Get all questions available to students grade level 
        val questions = for {
          statement <- statements
          q = Questions.findByStatement(statement.id.get)
        } yield q._1
        val flatQuestions = questions.flatten

        // fiter out the questions already asked and answered
        val filteredQuestions = flatQuestions.filter(q => {
          QuestionScores.findByQuestionAndStudent(q.id.get, studentId) match {
            case Some(questionScore) => false
            case None => true
          }
        })
        // now choose a random one to ask
        // in the future, this needs to be smarter but MVP is asking a random question
        val finalQuestion = filteredQuestions(random.nextInt(filteredQuestions.length))
        Questions.convertToJsonQuestion(finalQuestion, None)
      } catch {
        case e: java.util.NoSuchElementException => JsonQuestion(Some(-1), "No questions available!", None, None, None, None)
        case e2: Throwable => JsonQuestion(Some(-1), e2.getMessage(), None, None, None, None)
      }
    }
  }
}
