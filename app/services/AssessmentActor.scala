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

import akka.actor.Actor
import models._
import play.api.Play.current
import play.api.db.slick.DB
import play.Logger

import scala.compat.Platform
import scala.util.Random

case class ChildAndStandard(childId: Long, standardId: Long)
case class CreateAssessment(parentUid: Long, childId: Long, standardId: Long)
case class ScoreAssessment(assessmentId: Long, studentId: Long, questionId: Long, standardId: Long, score: Long)
case class AssessmentQuestion(assessment: Assesment, question: Question)
case class TestAssessment(e: String)

class AssessmentActor extends Actor {

  private val random = new Random

  def receive = {
    case childAndStandard: ChildAndStandard => {
      val asses = DB.withSession { implicit s =>
        Assesments.create(Assesment(None, childAndStandard.childId, Platform.currentTime, None))
      }
      sender ! None
    }
    case te: TestAssessment => {
      sender ! "Hello"
    }
    case assessment: CreateAssessment => {
      sender ! create(assessment)
    }
    case scoreAssessment: ScoreAssessment => {
      sender ! score(scoreAssessment)
    }
    case _ => sender ! akka.actor.Status.Failure(new Exception("nothing sent to do"))
  }

  /**
   * Finds a random question in the database based on the education level of the child,
   * the standard that was asked for
   * and that the child has not been scored on the question yet
   */
  def create(createAssessment: CreateAssessment): Option[AssessmentQuestion] = {
    DB.withSession { implicit s =>
      //PersonService.isChildOf[AssessmentQuestion](createAssessment.parentUid,
      //createAssessment.childId, cid => {
      // this is a new assessment, so create a new assessment record
      val assessment = Assesments.create(Assesment(None, createAssessment.childId, Platform.currentTime, None))
      val question = chooseQuestion(
        createAssessment.childId, createAssessment.standardId, true, None) getOrElse (Question(Some(-1L), "", None, None, None))
      Some(AssessmentQuestion(assessment, question))
      // })
    }
  }

  def score(scoreAssessment: ScoreAssessment): Option[AssessmentQuestion] = {
    DB.withSession { implicit s =>
      // Get activty for this question...
      Questions.findActivity(scoreAssessment.questionId) match {
        case Some(activity) => {
          val hws = activity.id map { activityId =>
            // Create the score in the database...
            val score = Scores.create(
              Score(None, scoreAssessment.studentId, None, None, Some(activityId), Some(scoreAssessment.score), Platform.currentTime)
            )
            AssessmentHistories.create(AssessmentHistory(None, scoreAssessment.assessmentId, activityId, score.id.getOrElse(0)))
            val history = AssessmentHistories.findByAssessmentWithScore(scoreAssessment.assessmentId)
            history;
          }
          hws match {
            case Some(history) => determineNextQuestion(history, history.length - 1, scoreAssessment)
            case _ => None
          }
        }
        case _ => None
      }
    }
  }

  def determineNextQuestion(history: List[AssessmentHistoryWithScore], place: Int, scoreAssessment: ScoreAssessment): Option[AssessmentQuestion] = {
    DB.withSession { implicit s =>

      val lastScore = history(place).score
      val lastScoreValue = history(place).score map (_.score) getOrElse 0
      if (lastScoreValue != 3L) {
        val assessment = Assesments.find(scoreAssessment.assessmentId)
        val question = chooseQuestion(scoreAssessment.studentId, scoreAssessment.standardId, true, lastScore)
        //TODO: Perform matching so .get doesn't fail!
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
  def chooseQuestion(studentId: Long, standardId: Long, isRandom: Boolean, lastScored: Option[Score]): Option[Question] = {
    DB.withSession { implicit s =>
      val activities = Activities.filterByStandardLevelCategory(studentId, standardId, "question")
        .filter(activity => {
          activity.id map { aid =>
            Scores.findByActivityAndStudent(aid, studentId) match {
              case Some(activ) => false
              case _ => true
            }
          } getOrElse true
        })
      if (activities.length < 1) {
        None
      } else {
        // if lastScored is set, than we need to filter again by only statements that are appropriate
        val updatedActivities = lastScored.map { score =>
          // get the score and activity and determine which statement it was scored for...
          Scores.find(score.id getOrElse (0)) match {
            case Some(sc) => {
              val currentSequence: Long = Activities.findWithStatements(sc.activityId.get) match {
                case Some(aws) => aws.statements(0).sequence.getOrElse(1L)
                case None => 0
              }
              //val availableStatements = Statements.filterByStandardAndLevel(standardId, studentId); 
              Logger.debug(s"last scored was ${sc.score}");
              Logger.debug(s"current statement sequence is $currentSequence")
              val sequenceToUse = sc.score match {
                case Some(1) => if (currentSequence > 1) currentSequence - 2 else 1
                case Some(2) => if (currentSequence > 0) currentSequence - 1 else 1
                case Some(3) => currentSequence
                case Some(4) => currentSequence + 1
                case Some(5) => currentSequence + 2
                case _ => 1
              }
              Logger.debug(s"sequence to use is $sequenceToUse")
              // get the statement with the above sequence, gradelevel and standard...
              Statements.findBySequence(sequenceToUse, standardId, studentId) map { statement =>
                Logger.debug(s"statement to use $statement")
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
