package models

import java.util.Date
import java.sql.Timestamp
import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
import scala.slick.SlickException
import scala.slick.lifted.ProvenShape
import play.api.Play.current
import play.api.Logger

case class Standard(
  id: Option[Long],
  organizationId:Option[Long],
  title: String,
  description: String,
  publicationStatus: String,
  subject: String,
  educationLevels:Option[String],
  language:Option[String],
  source: Option[String],
  dateValid: Option[Date],
  repositoryDate: Option[Date],
  rights: Option[String],
  manifest: Option[String]
)
class Standards(tag: Tag) extends Table[Standard](tag, "standards"){
  
  implicit val JavaUtilDateMapper = {
    MappedColumnType .base[java.util.Date, java.sql.Timestamp] (
      d => new java.sql.Timestamp(d.getTime),
      d => new java.util.Date(d.getTime)) 
  }
  
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def organizationId = column[Option[Long]]("organization_id")
  def title = column[String]("title")
  def description = column[String]("description")
  def publicationStatus = column[String]("publication_status")
  def subject = column[String]("subject")
  def educationLevels = column[Option[String]]("education_levels")
  def language = column[Option[String]]("language")
  def source = column[Option[String]]("source")
  def dateValid = column[Option[Date]]("date_valid")
  def repositoryDate = column[Option[Date]]("repository_date")
  def rights = column[Option[String]]("rights")
  def manifest = column[Option[String]]("manifest")

  def * = (id.?,
           organizationId,
           title,
           description,
           publicationStatus,
           subject,
           educationLevels,
           language,
           source,
           dateValid,
           repositoryDate,
           rights,
           manifest
           ) <> ( Standard.tupled,Standard.unapply _)
}

object Standards {
  
  val standards = TableQuery[Standards]

  def insert(standard: Standard)(implicit s: Session) =
    (standards returning standards.map(_.id) into ((st,id) => st.copy(id=Some(id)))) += standard

  def update(id: Long, standard: Standard)(implicit s: Session) =
    standards.filter(_.id === id).update(standard.copy(Some(id))) 

  def find(id: Long)(implicit s: Session) = 
    standards.filter(_.id === id).firstOption

  def find(title: String)(implicit s: Session) =
    standards.filter(_.title like "%" + title + "%").list

  def list(implicit s: Session) = 
    standards.list

  def delete(standard: Standard)(implicit s: Session): Int = 
    standards.filter(_.id === standard.id.get).delete
}
