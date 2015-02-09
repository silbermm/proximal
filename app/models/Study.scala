package models;

import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
import scala.slick.SlickException
import scala.slick.lifted.ProvenShape
import play.api.Play.current
import play.api.Logger

case class Study(id: Option[Long],
  activityId: Long,
  studentId: Long,
  status: Option[String],
  dateStarted: Long)

class Studies(tag: Tag) extends Table[Study](tag, "study") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def activityId = column[Long]("activity_id")
  def studentId = column[Long]("student_id")
  def status = column[Option[String]]("status")
  def dateStarted = column[Long]("date_started")

  def * = (id.?, activityId, studentId, status, dateStarted) <> (Study.tupled, Study.unapply _)

  def activity = foreignKey("study_activity_fk", activityId, Activities.activities)(_.id)
  def student = foreignKey("student_study_activity_fk", studentId, People.people)(_.id)

}

object Studies {
  lazy val studies = TableQuery[Studies]

  def create(study: Study)(implicit s: Session): Study =
    (studies returning studies.map(_.id) into ((st, id) => st.copy(Some(id)))) += study

  def delete(study: Study)(implicit s: Session): Int =
    studies.filter(_.id === study.id.get).delete

  def all(implicit s: Session) =
    studies.list

  def find(id: Long)(implicit s: Session) =
    studies.filter(_.id === id).firstOption

  def findByStudent(studentId: Long)(implicit s: Session) =
    studies.filter(_.studentId === studentId).list

}