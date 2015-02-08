package models

import java.util.Date
import java.sql.Timestamp
import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
import scala.slick.SlickException
import scala.slick.lifted.ProvenShape
import play.api.Play.current
import play.api.Logger

case class EducationLevel(id: Option[Long], value: String, description: String)
class EducationLevels(tag: Tag) extends Table[EducationLevel](tag, "education_levels") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def value = column[String]("value")
  def description = column[String]("description")
  def * = (id.?, value, description) <> (EducationLevel.tupled, EducationLevel.unapply _)

  def idx = index("idx_value", (value), unique = true)
}

object EducationLevels {

  lazy val education_levels = TableQuery[EducationLevels]
  lazy val standards = Standards.standards
  lazy val standard_levels = StandardLevels.standard_levels
  lazy val statement_levels = StatementLevels.statement_levels

  def insert(e: EducationLevel)(implicit s: Session): EducationLevel = {
    find(e.description).getOrElse((education_levels returning education_levels.map(_.id) into ((level, id) => level.copy(id = Some(id)))) += e)
  }

  def insert(e: Seq[EducationLevel])(implicit s: Session): Option[Int] =
    education_levels ++= e

  def update(id: Long, e: EducationLevel)(implicit s: Session): Int = {
    education_levels.filter(_.id === id).update(e.copy(Some(id)))
  }

  def list(implicit s: Session) =
    education_levels.list

  def find(id: Long)(implicit s: Session) =
    education_levels.filter(_.id === id).firstOption

  def find(description: String)(implicit s: Session) =
    education_levels.filter(_.description === description).firstOption

  def findWithStandards(id: Long)(implicit s: Session): List[(EducationLevel, Standard)] = {
    val query = for {
      l <- standard_levels if l.educationLevelId === id
      e <- l.educationLevel
      s <- l.standard
    } yield (e, s)
    return query.list
  }

  def findWithStatements(id: Long)(implicit s: Session): List[(EducationLevel, Statement)] = {
    val query = for {
      l <- statement_levels if l.educationLevelId === id
      e <- l.educationLevel
      s <- l.statement
    } yield (e, s)
    return query.list
  }

  def findByChild(child: Person)(implicit s: Session): Option[EducationLevel] = {
    child.educationLevelId match {
      case Some(i) => education_levels.filter(_.id === i).firstOption
      case _ => None
    }
  }

  def delete(e: EducationLevel)(implicit s: Session): Int = {
    education_levels.filter(_.id === e.id.get).delete
  }

}

case class StatementLevel(id: Option[Long], educationLevelId: Long, statementId: Long)
class StatementLevels(tag: Tag) extends Table[StatementLevel](tag, "statement_levels") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def educationLevelId = column[Long]("education_level_id")
  def statementId = column[Long]("statement_id")

  def * = (id.?, educationLevelId, statementId) <> (StatementLevel.tupled, StatementLevel.unapply _)

  def educationLevel = foreignKey("education_level_statement", educationLevelId, EducationLevels.education_levels)(_.id)
  def statement = foreignKey("statement", statementId, Statements.statements)(_.id)

}

object StatementLevels {
  lazy val statement_levels = TableQuery[StatementLevels]

  def insert(sl: StatementLevel)(implicit s: Session): StatementLevel =
    (statement_levels returning statement_levels.map(_.id) into ((statementLevel, id) => statementLevel.copy(id = Some(id)))) += sl

  def delete(sl: StatementLevel)(implicit s: Session): Int =
    statement_levels.filter(_.id === sl.id.get).delete

  def update(id: Long, sl: StatementLevel)(implicit s: Session): Int =
    statement_levels.filter(_.id === id).update(sl.copy(Some(id)))

  def find(id: Long)(implicit s: Session) =
    statement_levels.filter(_.id === id).firstOption

  def findByEducationLevelId(id: Long)(implicit s: Session) =
    statement_levels.filter(_.educationLevelId === id).list

  def findByStatementId(id: Long)(implicit s: Session) =
    statement_levels.filter(_.statementId === id).list

  def findStatements(edLevelId: Long)(implicit s: Session): List[Statement] = {
    val q = for {
      level <- statement_levels if level.educationLevelId === edLevelId
      statement <- level.statement
    } yield statement
    q.list
  }

}

case class StandardLevel(id: Option[Long], educationLevelId: Long, standardId: Long)
class StandardLevels(tag: Tag) extends Table[StandardLevel](tag, "standard_levels") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def educationLevelId = column[Long]("education_level_id")
  def standardId = column[Long]("standard_id")

  def * = (id.?, educationLevelId, standardId) <> (StandardLevel.tupled, StandardLevel.unapply _)

  def educationLevel = foreignKey("education_level_standard", educationLevelId, EducationLevels.education_levels)(_.id)
  def standard = foreignKey("standard", standardId, Standards.standards)(_.id)

}

object StandardLevels {
  lazy val standard_levels = TableQuery[StandardLevels]

  def insert(sl: StandardLevel)(implicit s: Session): StandardLevel =
    (standard_levels returning standard_levels.map(_.id) into ((standardLevel, id) => standardLevel.copy(id = Some(id)))) += sl

  def delete(sl: StandardLevel)(implicit s: Session): Int =
    standard_levels.filter(_.id === sl.id.get).delete

  def update(id: Long, sl: StandardLevel)(implicit s: Session): Int =
    standard_levels.filter(_.id === id).update(sl.copy(Some(id)))

  def find(id: Long)(implicit s: Session) =
    standard_levels.filter(_.id === id).firstOption

  def findByEducationLevelId(id: Long)(implicit s: Session) =
    standard_levels.filter(_.educationLevelId === id).firstOption

  def findByStandardId(id: Long)(implicit s: Session) =
    standard_levels.filter(_.standardId === id).firstOption

  def findListByStandardId(id: Long)(implicit s: Session) =
    standard_levels.filter(_.standardId === id).list
}
