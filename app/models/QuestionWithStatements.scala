package models

import java.util.Date
import java.sql.Timestamp
import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
import scala.slick.SlickException
import scala.slick.lifted.ProvenShape
import play.api.Play.current
import play.api.Logger

case class QuestionWithStatements(id: Option[Long], questionId: Long, statementId: Long)
class QuestionsWithStatements(tag: Tag) extends Table[QuestionWithStatements](tag, "questions_with_statements") {

  def id = column[Long]("id", O.PrimaryKey,O.AutoInc)
  def questionId = column[Long]("question_id")
  def statementId = column[Long]("statement_id")

  def * = (id.?, questionId, statementId) <> (QuestionWithStatements.tupled, QuestionWithStatements.unapply _)

  def question = foreignKey("question_fk", questionId, Questions.questions)(_.id)
  def statement = foreignKey("statement_fk", statementId, Statements.statements)(_.id)
}

object QuestionsWithStatements {
  
  lazy val questions_with_statements = TableQuery[QuestionsWithStatements]

  def create(qs:QuestionWithStatements)(implicit s: Session) : QuestionWithStatements = {
    ???
  }

  def update(qs: QuestionWithStatements)(implicit s: Session): Int = {
    ???
  }

  def findByQuestionId(qid: Long)(implicit s: Session) : List[QuestionWithStatements] = {
    ???
  }

  def findByStatementId(sid: Long)(implicit s: Session) : List[QuestionWithStatements] = {
    ???
  }

  def delete(id: Long)(implicit s: Session) : Int = {
    ???
  }
}
