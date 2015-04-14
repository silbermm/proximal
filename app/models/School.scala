package models

import java.util.Date

import play.api.db.slick.Config.driver.simple._

import scala.slick.lifted.Tag

case class Attendence(personId: Long, schoolId: Long, startDate: Option[Date], endDate: Option[Date], grade: Option[Long])
class Attendences(tag: Tag) extends Table[Attendence](tag, "attendence") {

  implicit val JavaUtilDateMapper = {
    MappedColumnType.base[java.util.Date, java.sql.Timestamp](
      d => new java.sql.Timestamp(d.getTime),
      d => new java.util.Date(d.getTime))
  }

  def personId = column[Long]("personId")
  def schoolId = column[Long]("schoolId")
  def startDate = column[Option[Date]]("startDate")
  def endDate = column[Option[Date]]("endDate")
  def grade = column[Option[Long]]("grade")

  def * = (personId, schoolId, startDate, endDate, grade) <> (Attendence.tupled, Attendence.unapply _)
}

case class School(id: Option[Long], name: String, streetNumber: String, street: String, city: String, state: Option[String], district: Option[String], public: Option[Int])
class Schools(tag: Tag) extends Table[School](tag, "school") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def streetNumber = column[String]("streetNumber")
  def street = column[String]("street")
  def city = column[String]("city")
  def state = column[Option[String]]("state")
  def district = column[Option[String]]("district")
  def public = column[Option[Int]]("public")

  def * = (id.?, name, streetNumber, street, city, state, district, public) <> (School.tupled, School.unapply _)
}

object Schools {
  val schools = TableQuery[Schools]

  def insert(school: School)(implicit s: Session): School =
    (schools returning schools.map(_.id) into ((s, id) => s.copy(id = Some(id)))) += school

  def findById(id: Long)(implicit s: Session) =
    schools.filter(_.id === id).firstOption

  def findByNameCityState(name: String, city: String, state: String)(implicit s: Session) =
    schools.filter(x => x.name === name && x.city === city && x.state === state).firstOption

  def findByCity(city: String)(implicit s: Session) =
    schools.filter(_.city like s"%$city%").list

  def update(id: Long, school: School)(implicit s: Session) = {
    val schoolToUpdate = school.copy(Some(id))
    schools.filter(_.id === id).update(schoolToUpdate)
  }

}

object Attendences {
  val attendences = TableQuery[Attendences]

  def insert(attendence: Attendence)(implicit s: Session) =
    (attendences returning attendences.map(_.personId)) += attendence

}
