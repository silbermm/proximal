package services

import play.api.Logger
import models._
import play.api.db.slick.DB
import play.api.Play.current
import scala.util.Random

object AssesmentService {

  private val random = new Random

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
