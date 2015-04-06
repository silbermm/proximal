package models

import play.api.db.slick.Config.driver.simple._

import scala.slick.lifted.Tag

case class Assesment(id: Option[Long], studentId: Long, startDate: Long, endDate: Option[Long])
case class AssesmentWithQuestionsAndScores(id: Long,
  startDate: Long,
  endDate: Option[Long])

class Assesments(tag: Tag) extends Table[Assesment](tag, "assesments") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def studentId = column[Long]("studentId")
  def startDate = column[Long]("startDate")
  def endDate = column[Option[Long]]("endDate")

  def * = (id.?, studentId, startDate, endDate) <> (Assesment.tupled, Assesment.unapply _)

  def student = foreignKey("assessment_student_fk", studentId, People.people)(_.id)
}

object Assesments {

  lazy val assesments = TableQuery[Assesments]

  def create(qs: Assesment)(implicit s: Session): Assesment =
    (assesments returning assesments.map(_.id) into ((assesment, id) => assesment.copy(Some(id)))) += qs

  def delete(id: Long)(implicit s: Session): Int =
    assesments.filter(_.id === id).delete

  def find(id: Long)(implicit s: Session): Option[Assesment] =
    assesments.filter(_.id === id).firstOption
}
