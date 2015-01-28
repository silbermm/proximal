  package services

import play.api.Logger
import models._
import play.api.db.slick.DB
import play.api.Play.current
import scala.util.Random
import play.api._
import play.api.mvc._
import scala.compat.Platform


object AssesmentService {

  private val random = new Random

  def newAssesment(studentId: Long) : (Assesment, JsonQuestion) = {
    val asses = DB.withSession{ implicit s =>
      Assesments.create(Assesment(None,studentId, Platform.currentTime,None))
    }
    (asses, nextQuestion(studentId))
  }

  def createQuestionScore(assesmentId: Long, questionScore: QuestionScore,studentId: Long) : (Assesment, JsonQuestion) = {
    DB.withSession {implicit s =>
      val qscore = QuestionScores.create(questionScore); 
      AssesmentQuestionScores.create(AssesmentQuestionScore(None,assesmentId, qscore.id.get))
      (Assesments.find(assesmentId).get, nextQuestion(studentId));
    }
  }

  /**
   * Find the assessment and return the child's id that 
   * is taking this assessment
   */
  def findChildForAssessment(assessmentId: Long) : Long = {
    DB.withSession{ implicit s =>
      Assesments.find(assessmentId) match {
        case Some(assess) => {
          assess.studentId
        }
        case None => -1
      }

    }

  }

  def nextQuestion(studentId:Long) : JsonQuestion = {
    DB.withSession{ implicit s => 
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
      val filteredQuestions = flatQuestions.filter( q => {
        QuestionScores.findByQuestionAndStudent(q.id.get, studentId) match {
          case Some(questionScore) => false
          case None => true
        }
      })    
      // now choose a random one to ask
      // in the future, this needs to be smarter but MVP is asking a random question
      val finalQuestion = filteredQuestions(random.nextInt(filteredQuestions.length))
      Questions.convertToJsonQuestion(finalQuestion, None)       
    } 
  }
}
