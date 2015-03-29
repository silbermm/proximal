package models

import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
import scala.slick.SlickException
import scala.slick.lifted.ProvenShape
import play.api.Play.current
import play.api.Logger

case class Attempt(id: Option[Long], activityId: Long, studentId: Long, timestamp: Long, score: Long)

class Attempts(tag: Tag) extends Table[Attempt](tag, "attempts") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def activityId = column[Long]("activity_id")
  def studentId = column[Long]("student_id")
  def timestamp = column[Long]("timestamp")
  def score = column[Long]("score")

  def * = (id.?, activityId, studentId, timestamp, score) <> (Attempt.tupled, Attempt.unapply _)

  def activity = foreignKey("attempts_activity_fk", activityId, Activities.activities)(_.id)
  def student = foreignKey("attempts_student_fk", studentId, People.people)(_.id)
}

object Attempts {
  val attempts = TableQuery[Attempts]

  def create(attempt: Attempt)(implicit s: Session) =
    (attempts returning attempts.map(_.id) into ((a, id) => a.copy(Some(id)))) += attempt

  def delete(attempt: Attempt)(implicit s: Session) =
    attempts.filter(_.id === attempt.id.get).delete

  def update = ???

  def find = ???

  def findByStudentAndActivity(studentId: Long, activityId: Long)(implicit s: Session) =
    attempts.filter(x => x.studentId === studentId && x.activityId === activityId).firstOption

  def findByStudentAndActivity = ???

}
