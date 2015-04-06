package models

import play.api.db.slick.Config.driver.simple._

import scala.slick.lifted.Tag

case class Score(id: Option[Long], studentId: Long, questionId: Option[Long], actId: Option[Long], activityId: Option[Long], score: Option[Long], timestamp: Long)

case class ScoreQuestion(
  scoreId: Long,
  score: Option[Long],
  timestamp: Long,
  question: JsonQuestion)

case class ScoreAct(
  scoreId: Long,
  score: Option[Long],
  timestamp: Long,
  act: Act)

case class ScoreActivity(
  scoreId: Long,
  score: Option[Long],
  timestamp: Long,
  activity: Activity)

class Scores(tag: Tag) extends Table[Score](tag, "scores") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def studentId = column[Long]("studentId")
  def questionId = column[Option[Long]]("questionId")
  def actId = column[Option[Long]]("actid")
  def activityId = column[Option[Long]]("activity_id")
  def score = column[Option[Long]]("compentency")
  def timestamp = column[Long]("timestamp")

  def * = (id.?, studentId, questionId, actId, activityId, score, timestamp) <> (Score.tupled, Score.unapply _)

  def student = foreignKey("scores_student_fk", studentId, People.people)(_.id)
  def question = foreignKey("scores_question_fk", questionId, Questions.questions)(_.id)
  def act = foreignKey("scores_acts_fk", actId, Acts.acts)(_.id)
  def activity = foreignKey("scores_activity_fk", activityId, Activities.activities)(_.id)
}

object Scores {

  lazy val scores = TableQuery[Scores]
  lazy val questions = Questions.questions
  lazy val acts = Acts.acts
  lazy val activities = Activities.activities

  def create(qs: Score)(implicit s: Session): Score =
    (scores returning scores.map(_.id) into ((scores, id) => scores.copy(Some(id)))) += qs

  def update(qs: Score)(implicit s: Session): Int =
    scores.filter(_.id === qs.id.get).update(qs)

  def find(id: Long)(implicit s: Session): Option[Score] =
    scores.filter(_.id === id).firstOption

  def findByStudent(studentId: Long)(implicit s: Session) =
    scores.filter(_.studentId === studentId).list

  def findByQuestion(questionId: Long)(implicit s: Session) =
    scores.filter(_.questionId === questionId).list

  def findByAct(actId: Long)(implicit s: Session) =
    scores.filter(_.actId === actId).list

  def findByQuestionAndStudent(questionId: Long, studentId: Long)(implicit s: Session) = {
    scores.filter(x => x.questionId === questionId && x.studentId === studentId).firstOption
  }

  def findByActAndStudent(actId: Long, studentId: Long)(implicit s: Session) = {
    scores.filter(x => x.actId === actId && x.studentId === studentId).firstOption
  }

  def findByActivityAndStudent(activityId: Long, studentId: Long)(implicit s: Session) = {
    scores.filter(x => x.activityId === activityId && x.studentId === studentId).firstOption
  }

  def findWithQuestions(id: Long)(implicit s: Session): ScoreQuestion = {
    val query = for {
      qs <- scores if qs.id === id
      q <- qs.question
    } yield (qs, q)
    val resp = query.first
    ScoreQuestion(id, resp._1.score, resp._1.timestamp, Questions.convertToJsonQuestion(resp._2, None, None))
  }

  def findByStudentWithQuestions(studentId: Long)(implicit s: Session): List[ScoreQuestion] = {
    val query = for {
      qs <- scores if qs.studentId === studentId
      q <- qs.question
    } yield (qs, q)
    val resp = query.list
    resp.map { old =>
      ScoreQuestion(old._1.id.get, old._1.score, old._1.timestamp, Questions.convertToJsonQuestion(old._2, None, None))
    }
  }

  def delete(id: Long)(implicit s: Session): Int =
    scores.filter(_.id === id).delete

  def all(implicit s: Session): List[Score] =
    scores.list

  def allWithQuestions(implicit s: Session): List[ScoreQuestion] = {
    all.map { qs =>
      findWithQuestions(qs.id.get)
    }
  }
}

