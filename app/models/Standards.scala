package models

import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag

case class Standard(
  id: Option[Long],
  title: String,
  description: String,
  subject: String)

class Standards(tag: Tag) extends Table[Standard](tag, "standards") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def title = column[String]("title")
  def description = column[String]("description", O.DBType("Text"))
  def subject = column[String]("subject")

  def * = (id.?, title, description, subject) <> (Standard.tupled, Standard.unapply _)
}

object Standards {

  lazy val standards = TableQuery[Standards]
  lazy val education_levels = EducationLevels.education_levels
  lazy val standard_levels = EducationLevels.standard_levels
  lazy val statement_levels = StatementLevels.statement_levels
  lazy val statements = Statements.statements

  def insert(standard: Standard)(implicit s: Session) =
    (standards returning standards.map(_.id) into ((st, id) => st.copy(id = Some(id)))) += standard

  def update(id: Long, standard: Standard)(implicit s: Session) =
    standards.filter(_.id === id).update(standard.copy(Some(id)))

  def find(id: Long)(implicit s: Session) =
    standards.filter(_.id === id).firstOption

  def find(title: String)(implicit s: Session) =
    standards.filter(_.title like "%" + title + "%").list

  def list(implicit s: Session) =
    standards.list

  def findWithEducationLevels(id: Long)(implicit s: Session): (Option[Standard], List[EducationLevel]) = {
    val query = for {
      l <- standard_levels if l.standardId === id
      e <- l.educationLevel
      s <- l.standard
    } yield e
    (find(id), query.list)
  }

  def findWithStatements(id: Long)(implicit s: Session): (Option[Standard], List[Statement]) = {
    val qs = statements.filter(_.standardId === id)
    (find(id), qs.list)
  }

  def delete(standard: Standard)(implicit s: Session): Int =
    standards.filter(_.id === standard.id.get).delete
}

