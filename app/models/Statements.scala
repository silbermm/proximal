package models

import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag

case class Statement(
  id: Option[Long],
  sequence: Option[Long],
  standardId: Option[Long],
  subject: Option[String],
  notation: Option[String],
  description: Option[String])

class Statements(tag: Tag) extends Table[Statement](tag, "statements") {

  lazy val standards = Standards.standards

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def sequence = column[Option[Long]]("sequence")
  def standardId = column[Option[Long]]("standardId")
  def subject = column[Option[String]]("subject")
  def notation = column[Option[String]]("notation")
  def description = column[Option[String]]("description", O.DBType("Text"))

  def * = (id.?, sequence, standardId, subject, notation, description) <> (Statement.tupled, Statement.unapply _)

  def standard = foreignKey("STANDARD_FK", standardId, standards)(_.id)
}

object Statements {

  lazy val statements = TableQuery[Statements]
  lazy val standards = Standards.standards
  lazy val statement_levels = StatementLevels.statement_levels
  lazy val people = People.people

  def insert(statement: Statement)(implicit s: Session) =
    (statements returning statements.map(_.id) into ((st, id) => st.copy(id = Some(id)))) += statement

  def insert(statement: Seq[Statement])(implicit s: Session) =
    statements ++= statement

  def update(id: Long, statement: Statement)(implicit s: Session) =
    statements.filter(_.id === id).update(statement.copy(Some(id)))

  def find(id: Long)(implicit s: Session) =
    statements.filter(_.id === id).firstOption

  def findWithStandard(id: Long)(implicit s: Session) = {
    val query = for {
      l <- statements if l.id === id
      e <- l.standard
    } yield e
    (find(id), query.firstOption)
  }

  def findWithEducationLevels(id: Long)(implicit s: Session): (Option[Statement], List[EducationLevel]) = {
    val query = for {
      l <- statement_levels if l.statementId === id
      e <- l.educationLevel
      s <- l.statement
    } yield e
    (find(id), query.list)
  }

  def findBySequence(sequenceNumber: Long, standardId: Long)(implicit s: Session): Option[Statement] =
    statements.filter(x => x.sequence === sequenceNumber && x.standardId === standardId).firstOption

  def list(implicit s: Session) =
    statements.list

  def delete(statement: Statement)(implicit s: Session) =
    statements.filter(_.id === statement.id.get).delete

}
