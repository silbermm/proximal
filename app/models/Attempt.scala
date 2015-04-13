package models

import play.api.db.slick.Config.driver.simple._

import scala.slick.lifted.Tag
import play.Logger

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
  lazy val attempts = TableQuery[Attempts]
  lazy val activities = Activities.activities
  lazy val standards = Standards.standards
  lazy val activityStatements = ActivityStatements.activityStatements

  def create(attempt: Attempt)(implicit s: Session) =
    (attempts returning attempts.map(_.id) into ((a, id) => a.copy(Some(id)))) += attempt

  def delete(attempt: Attempt)(implicit s: Session) =
    attempts.filter(_.id === attempt.id.get).delete

  def findByStudentAndActivity(studentId: Long, activityId: Long)(implicit s: Session) =
    attempts.filter(x => x.studentId === studentId && x.activityId === activityId).firstOption

  def findByStudentAndStandard(studentId: Long, standardId: Long)(implicit s: Session) = {
    val query = for {
      attempt <- attempts if attempt.studentId === studentId
      activity <- activities if activity.id === attempt.activityId
      activityStatement <- activityStatements if activityStatement.activityId === activity.id
      statements <- activityStatement.statement if statements.standardId === standardId
    } yield attempt
    query.list
  }

}
