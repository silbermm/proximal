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
case class ScoreAssessment(assessmentId: Long, childId: Long, questionId: Long, standardId: Long, score: Long)
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
          val Some(question) = chooseQuestion(cid, createAssessment.standardId, true, None)
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
        val history = AssessmentHistories.findByAssessmentWithScore(scoreAssessment.assessmentId)
        // Decide if we need another question...
        determineNextQuestion(history, history.length - 1, scoreAssessment)
      } getOrElse None
    }
  }

  private def determineNextQuestion(history: List[AssessmentHistoryWithScore], place: Int, scoreAssessment: ScoreAssessment): Option[AssessmentQuestion] = {
    DB.withSession { implicit s =>
      val lastScore = history(place).score
      val lastScoreValue = history(place).score map (_.score) getOrElse 0
      if (lastScoreValue != 3L) {
        val assessment = Assesments.find(scoreAssessment.assessmentId)
        val question = chooseQuestion(scoreAssessment.childId, scoreAssessment.standardId, true, lastScore)
        Some(AssessmentQuestion(assessment.get, question.get))
      } else {
        if (history.length - 3 == place) {
          // we've checked the last three scores and they all equal 3, this is the standard we should recommend
          // for the next activity
          None
        } else {
          // the last score value was 3, what about the one before and the one before that?
          determineNextQuestion(history, place - 1, scoreAssessment)
        }
      }
    }
  }

  /**
   * This does the work of choosing the next question to ask
   */
  private def chooseQuestion(studentId: Long, standardId: Long, isRandom: Boolean, lastScored: Option[Score]): Option[Question] = {
    DB.withSession { implicit s =>
      val activities = Activities.filterByStandardLevelCategory(studentId, standardId, "question")
        .filter(activity => {
          val score = activity.id map (Scores.findByActivityAndStudent(_, studentId))
          score.isEmpty
        })
      if (activities.length < 1) {
        None
      } else {
        // if lastScored is set, than we need to filter again by only statements that are appropriate
        val updatedActivities = lastScored.map { score =>
          // got get the score and activity and determine which statement it was scored for...
          Scores.find(score.id getOrElse (0)) match {
            case Some(sc) => {
              val currentSequence = Activities.findWithStatements(sc.activityId.get) match {
                case Some(thing) => thing.statements.head.sequence.get
                case None => 0
              }
              //val availableStatements = Statements.filterByStandardAndLevel(standardId, studentId); 
              val sequenceToUse = sc.score match {
                case Some(1) => if (currentSequence > 1) currentSequence - 2 else 1
                case Some(2) => if (currentSequence > 0) currentSequence - 1 else 1
                case Some(3) => currentSequence
                case Some(4) => currentSequence + 1
                case Some(5) => currentSequence + 2
                case None => 1
              }
              // get the statement with the above sequence...
              Statements.findBySequence(sequenceToUse, standardId) map { statement =>
                activities.filter(activity => {
                  Activities.includesStatement(statement.id.get, activity.id.get)
                })
              } getOrElse {
                activities
              }
            }
            case None => activities
          }

        } getOrElse activities

        // choose a random activity and get the question for it..a
        if (isRandom) {
          updatedActivities(random.nextInt(activities.length)).resourceId map (x =>
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
