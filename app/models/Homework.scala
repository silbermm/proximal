package models

import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
import scala.slick.SlickException
import scala.slick.lifted.ProvenShape
import play.api.Play.current
import play.api.Logger

case class Homework(id: Option[Long],
  activityId: Option[Long],
  studentId: Option[Long],
  teacherId: Option[Long],
  status: String,
  dateGiven: Long,
  dateDue: Option[Long])

case class HomeworkActivityActs(
  homework: Homework,
  activity: Activity,
  acts: List[Option[ActWithScore]])

class Homeworks(tag: Tag) extends Table[Homework](tag, "homework") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def activityId = column[Option[Long]]("activity_id")
  def studentId = column[Option[Long]]("student_id")
  def teacherId = column[Option[Long]]("teacher_id")
  def status = column[String]("status")
  def dateGiven = column[Long]("date_given")
  def dateDue = column[Option[Long]]("date_due")

  def * = (id.?, activityId, studentId, teacherId, status, dateGiven, dateDue) <> (Homework.tupled, Homework.unapply _)

  def activity = foreignKey("homework_activity_fk", activityId, Activities.activities)(_.id)
  def student = foreignKey("homework_student_fk", studentId, People.people)(_.id)
  def teacher = foreignKey("homework_teacher_fk", teacherId, People.people)(_.id)

}

object Homeworks {
  lazy val homeworks = TableQuery[Homeworks]
  lazy val activityActs = ActivityActs.activityActs

  def create(h: Homework)(implicit s: Session): Homework =
    (homeworks returning homeworks.map(_.id) into ((hw, id) => hw.copy(Some(id)))) += h

  def delete(h: Homework)(implicit s: Session): Int =
    homeworks.filter(_.id === h.id.get).delete

  def all(implicit s: Session) =
    homeworks.list

  def find(id: Long)(implicit s: Session) =
    homeworks.filter(_.id === id).firstOption

  def findByStudent(studentId: Long)(implicit s: Session) =
    homeworks.filter(_.studentId === studentId).list

  def findByStudentWithActivitiesAndActs(studentId: Long)(implicit s: Session): List[HomeworkActivityActs] = {
    val homeworkAndActivities = for {
      homework <- homeworks if homework.studentId === studentId
      activity <- homework.activity
    } yield (homework, activity)

    homeworkAndActivities.list.map(tup => {
      val acts = ActivityActs.findByActivity(tup._2.id.get)
      val listOfActs = for {
        act <- acts
      } yield Acts.findWithScore(act.id.get)
      HomeworkActivityActs(tup._1, tup._2, listOfActs)
    })

  }
}
