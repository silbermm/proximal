package models

import java.util.Date
import java.sql.Timestamp
import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
import scala.slick.SlickException
import scala.slick.lifted.ProvenShape
import play.api.Play.current
import play.api.Logger

case class QuestionScore(id: Option[Long], studentId: Long, questionId: Long, timestamp: Long)

class QuestionScores(tag: Tag) extends Table[QuestionScore](tag, "question_scores") { 
  def id = column[Long]("id", O.PrimaryKey,O.AutoInc)
  def studentId = column[Long]("studentId")
  def questionId = column[Long]("questionId")
  def timestamp = column[Long]("timestamp")

  def * = (id.?, studentId, questionId, timestamp)

  def student = foreignKey("student_fk", studentId, People.people)(_.id)
  def question = foreignKey("question_fk", questionId, Questions.questions)(_.id)

}

object QuestionScores {

  lazy val questionScores = TableQuery[QuestionScores]
  
  def create(qs: QuestionScore)(implicit s: Session) : QuestionScore =
    (questionScores returning questionScores.map(_.id) into ((questionScores,id) => questionScores.copy(Some(id)))) += qs
 
  def update(qs: QuestionScore)(implicit s: Session) : Int = 
    questionScores.filter(_.id === qs.id.get).update(qs)

  def find(id: Long)(implicit s: Session) : Option[QuestionScore] =
    questionScores.filter(_.id === id).firstOption

  def findByStudent(studentId: Long)(implicit s: Session) =
    questionScores.filter(_.studentId === studentId).list

  def findByQuestion(questionId: Long)(implicit s: Session) = 
    questionScores.filter(_.questionId === questionId).list
  
  def delete(id: Long)(implicit s: Session) : Int = 
    questionScores.filter(_.id === id).delete

}

