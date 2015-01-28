package services

import play.api.libs.concurrent.Akka
import play.api.Logger
import services._
import models.actors._
import models._
import play.api.db.slick.DB
import play.api.Play.current
import scala.util.Random
import play.api._
import play.api.mvc._
import scala.compat.Platform
import helpers.ImplicitJsonFormat._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import play.api._
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.util.Random

case class AssessmentQuestion(assessment: Assesment, question: JsonQuestion)

class AssessmentActor extends Actor {

	private val random = new Random

	def receive = {
		case parentChild: ParentAndChild => {			
			val asses = DB.withSession{ implicit s =>
				Assesments.create(Assesment(None,parentChild.childId, Platform.currentTime,None))
			}    	
			sender ! Some(AssessmentQuestion(asses, nextQuestion(parentChild.childId)))		
		}
		case _ => sender ! None
	}


	def nextQuestion(studentId:Long) : JsonQuestion = {
	    DB.withSession{ implicit s => 
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
		    } catch {
		    	case e: java.util.NoSuchElementException => JsonQuestion(None,"No questions available!", None,None,None, None)
		    	case _ => JsonQuestion(None,"No questions available!", None,None,None,None)
		    }     
	  	}
	} 
}