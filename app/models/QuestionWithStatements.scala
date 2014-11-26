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

  def questions = foreignKey("question_fk", questionId, Questions.questions)(_.id)
  def statements = foreignKey("statement_fk", statementId, Statements.statements)(_.id)
}

object QuestionsWithStatements {
  
  lazy val qWithS = TableQuery[QuestionsWithStatements]

  def create(qs:QuestionWithStatements)(implicit s: Session) : QuestionWithStatements = {
    (qWithS returning qWithS.map(_.id) into ((q,id) => q.copy(Some(id)))) += qs
  }

  def update(qs: QuestionWithStatements)(implicit s: Session): Int = {
    qWithS.filter(_.id === qs.id.get).update(qs)
  }

  def findByQuestionId(qid: Long)(implicit s: Session) : List[QuestionWithStatements] = {
    qWithS.filter(_.questionId === qid).list
  }

  def findByStatementId(sid: Long)(implicit s: Session) : List[QuestionWithStatements] = {
    qWithS.filter(_.statementId === sid).list
  }

  def delete(id: Long)(implicit s: Session) : Int = {
    qWithS.filter(_.id === id).delete
  }
}
