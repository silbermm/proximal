package models

import java.sql.Timestamp
import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
import scala.slick.SlickException
import scala.slick.lifted.ProvenShape
import play.api.Play.current
import play.api.Logger
import java.sql.Timestamp
import scala.compat.Platform
import models._

case class Score(id: Option[Long], 
                 studentId: Long, 
                 questionId: Long, 
                 compentency: Int, 
                 timestamp: Timestamp)

class Scores(tag: Tag) extends Table[Score](tag, "scores"){
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def studentId = column[Long]("studentId")
  def questionId = column[Long]("questionId")
  def compentency = column[Int]("compentency")
  def timestamp = column[Timestamp]("timestamp", O.Default(new Timestamp(Platform.currentTime)))
  def * = (id.?, studentId, questionId, compentency, timestamp) <>  (Score.tupled, Score.unapply _)

  def student = foreignKey("student", studentId, People.people)(_.id)
  def question = foreignKey("question", questionId, Questions.questions)(_.id)

}

object Scores {
  
  val scores = TableQuery[Scores]
  
  def list(implicit s: Session)=
    scores.list

  def insert(score: Score)(implicit s: Session) =
    (scores returning scores.map(_.id) into ((s,id) => s.copy(id=Some(id)))) += score

  def update(score: Score)(implicit s: Session) =
    scores.filter(_.id === score.id.get).update(score)

  def delete(score: Score)(implicit s: Session) = 
    scores.filter(_.id === score.id.get).delete

  def find(id: Long)(implicit s: Session) = 
    scores.filter(_.id === id).firstOption

  def findByStudent(studentId: Long)(implicit s: Session) = {
    scores.filter(_.studentId === studentId).list 
  }
}


