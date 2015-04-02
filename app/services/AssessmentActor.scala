/* Copyright 2015 Matt Silbernagel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package services

import play.api.libs.concurrent.Akka
import play.api.Logger
import models._
import play.api.db.slick.DB
import play.api.Play.current
import scala.util.Random
import scala.compat.Platform
import akka.actor.{ Actor, ActorSystem, Props }
import play.api.libs.concurrent.Execution.Implicits.defaultContext

case class ChildAndStandard(childId: Long, standardId: Long)
case class CreateAssessment(parentUid: Long, childId: Long, standardId: Long)
case class ScoreAssessment(assessmentId: Long, childId: Long, questionId: Long, score: Long)
case class AssessmentQuestion(assessment: Assesment, question: Question)

class AssessmentActor extends Actor {

  private val random = new Random

  def receive = {
    /*case childAndStandard: ChildAndStandard => {*/
    //val asses = DB.withSession { implicit s =>
    //Assesments.create(Assesment(None, childAndStandard.childId, Platform.currentTime, None))
    //}
    //sender ! Some(AssessmentQuestion(asses, nextQuestion(asses, childAndStandard.childId, childAndStandard.standardId)))
    /*}*/
    case assessment: CreateAssessment => {
      create(assessment) map (sender ! _) getOrElse (sender ! akka.actor.Status.Failure(new Exception("failed to create")))
    }
    case scoreAssessment: ScoreAssessment => {
      sender ! score(scoreAssessment)
    }
    case _ => sender ! None
  }

  /**
   * Finds a random question in the database based on the education level of the child,
   * the standard that was asked for
   * and that the child has not been scored on the question yet
   */
  private def create(createAssessment: CreateAssessment): Option[AssessmentQuestion] = {
    DB.withSession { implicit s =>
      PersonService.isChildOf[AssessmentQuestion](createAssessment.parentUid,
        createAssessment.childId, cid => {
          // this is a new assessment, so create a new assessment record
          val assessment = Assesments.create(Assesment(None, cid, Platform.currentTime, None))
          val Some(question) = chooseQuestion(cid, createAssessment.standardId, true)
          Some(AssessmentQuestion(assessment, question))
        })
    }
  }

  private def score(scoreAssessment: ScoreAssessment): Option[AssessmentQuestion] = {
    DB.withSession { implicit s =>
      // Get activty for this question...
      Questions.findActivity(scoreAssessment.questionId) map { activity =>
        activity.id map { activityId =>
          // Create the score in the database...
          val score = Scores.create(Score(None, scoreAssessment.childId, None, None, Some(activityId), Some(scoreAssessment.score), Platform.currentTime))
          // Create the Assessment History... 
          AssessmentHistories.create(AssessmentHistory(None, scoreAssessment.assessmentId, activityId, score.id.get))
        }

        // Decide if we need another question...
        None
      } getOrElse None
    }
  }

  /**
   * This does the work of choosing the next question to ask
   */
  private def chooseQuestion(studentId: Long, standardId: Long, isRandom: Boolean): Option[Question] = {
    DB.withSession { implicit s =>
      val activities = Activities.filterByStandardLevelCategory(studentId, standardId, "question")
        .filter(activity => {
          val score = activity.id map (Scores.findByActivityAndStudent(_, studentId))
          score.isEmpty
        })
      if (activities.length < 1) {
        None
      } else {
        // choose a random activity and get the question for it..a
        if (isRandom) {
          activities(random.nextInt(activities.length)).resourceId map (x =>
            Questions.findByResourceId(x) getOrElse Question(Some(-1L), "", None, None, None)
          )
        } else {
          None
        }
      }
    }
  }

  private def nextQuestion(assessment: Assesment, studentId: Long, standardId: Long): JsonQuestion = {
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
          Scores.findByQuestionAndStudent(q.id.get, studentId) match {
            case Some(questionScore) => false
            case None => true
          }
        })
        // now choose a random one to ask
        // in the future, this needs to be smarter but MVP is asking a random question
        val finalQuestion = filteredQuestions(random.nextInt(filteredQuestions.length))
        Questions.convertToJsonQuestion(finalQuestion, None, None)
      } catch {
        case e: java.util.NoSuchElementException => JsonQuestion(Some(-1), "No questions available!", None, None, None, None, None)
        case e2: Throwable => {
          // Let's back out of our assessment since we can't ask any questions
          Assesments.delete(assessment.id.get)
          JsonQuestion(Some(-1), e2.getMessage(), None, None, None, None, None)
        }
      }
    }
  }
}
