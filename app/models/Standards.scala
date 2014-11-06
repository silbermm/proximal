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
  language:Option[String],
  source: Option[String],
  dateValid: Option[Date],
  repositoryDate: Option[Date],
  rights: Option[String],
  manifest: Option[String],
  identifier: Option[String]
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
  def description = column[String]("description", O.DBType("Text"))
  def publicationStatus = column[String]("publication_status")
  def subject = column[String]("subject")
  def language = column[Option[String]]("language")
  def source = column[Option[String]]("source")
  def dateValid = column[Option[Date]]("date_valid")
  def repositoryDate = column[Option[Date]]("repository_date")
  def rights = column[Option[String]]("rights")
  def manifest = column[Option[String]]("manifest")
  def identifier = column[Option[String]]("identifier")

  def * = (id.?,
           organizationId,
           title,
           description,
           publicationStatus,
           subject,
           language,
           source,
           dateValid,
           repositoryDate,
           rights,
           manifest,
           identifier
           ) <> ( Standard.tupled,Standard.unapply _)


}

object Standards {
  
  lazy val standards = TableQuery[Standards]
  lazy val education_levels = EducationLevels.education_levels
  lazy val standard_levels = EducationLevels.standard_levels

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

  def findWithEducationLevels(id: Long)(implicit s: Session):(Option[Standard], List[EducationLevel] ) = {
    val query = for {
      l <- standard_levels if l.standardId === id
      e <- l.educationLevel
      s <- l.standard
    } yield e
    
   return(find(id), query.list) 
  }
    

  def delete(standard: Standard)(implicit s: Session): Int = 
    standards.filter(_.id === standard.id.get).delete
}

case class Statement(
  id: Option[Long],
  standardId: Long, 
  asnUri: Option[String],
  subject: Option[String],
  notation: Option[String],
  alternateNotation: Option[String],
  label: Option[String],
  description: Option[String],
  alternateDescription: Option[String],
  exactMatch: Option[String],
  identifier: Option[String],
  language: Option[String]
)

class Statements(tag: Tag) extends Table[Statement](tag, "statements"){

  lazy val standards = Standards.standards

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def standardId = column[Long]("standardId")
  def asnUri = column[Option[String]]("asn_uri")
  def subject = column[Option[String]]("subject") 
  def notation = column[Option[String]]("notation")
  def alternateNotation = column[Option[String]]("alternate_notation")
  def label = column[Option[String]]("label") 
  def description = column[Option[String]]("description", O.DBType("Text"))
  def alternateDescription = column[Option[String]]("alternate_description", O.DBType("Text")) 
  def exactMatch = column[Option[String]]("exactMatch") 
  def identifier = column[Option[String]]("identifier") 
  def language = column[Option[String]]("language") 

  def * = (id.?,
           standardId,
           asnUri,
           subject,
           notation,
           alternateNotation,
           label,
           description,
           alternateDescription,
           exactMatch,
           identifier,
           language
           ) <> ( Statement.tupled,Statement.unapply _)
  
  def standard = foreignKey("STANDARD_FK", standardId, standards)(_.id)
}

object Statements {

  lazy val statements = TableQuery[Statements] 
  
  def insert(statement: Statement)(implicit s: Session) =
    (statements returning statements.map(_.id) into ((st,id) => st.copy(id=Some(id)))) += statement

  def update(id: Long, statement: Statement)(implicit s: Session) =
    statements.filter(_.id === id).update(statement.copy(Some(id))) 

  def find(id: Long)(implicit s: Session) = 
    statements.filter(_.id === id).firstOption

  def list(implicit s: Session) = 
    statements.list

}
