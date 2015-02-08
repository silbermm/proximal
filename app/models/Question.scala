package models

import org.apache.commons.codec.binary.Base64
import java.util.Date
import java.sql.Timestamp
import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
import scala.slick.SlickException
import scala.slick.lifted.ProvenShape
import play.api.Play.current
import play.api.Logger

case class Question(id: Option[Long], text: String, picture: Option[Array[Byte]], typeId: Option[Long], answer: Option[String])

case class JsonQuestion(id: Option[Long], text: String, picture: Option[String], typeId: Option[Long], answer: Option[String], statements: Option[List[Statement]])

class Questions(tag: Tag) extends Table[Question](tag, "questions") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def text = column[String]("text", O.DBType("Text"))
  def picture = column[Option[Array[Byte]]]("picture")
  def typeId = column[Option[Long]]("type_id")
  def answer = column[Option[String]]("answer", O.DBType("Text"))

  def * = (id.?, text, picture, typeId, answer) <> (Question.tupled, Question.unapply _)
}

object Questions {

  lazy val questions = TableQuery[Questions]
  lazy val questionsWithStatements = QuestionsWithStatements.qWithS

  def create(q: Question)(implicit s: Session): Question =
    (questions returning questions.map(_.id) into ((question, id) => question.copy(Some(id)))) += q

  def create(q: JsonQuestion)(implicit s: Session): JsonQuestion = {
    val question = create(convertToQuestion(q))
    convertToJsonQuestion(question, None)
  }

  def create(q: JsonQuestion, statements: List[Statement])(implicit s: Session): (Option[JsonQuestion], List[Statement]) = {
    create(q) match {
      case question: JsonQuestion => {
        val qs = for (st <- statements) yield QuestionWithStatements(None, question.id.get, st.id.get)
        for (st <- qs) QuestionsWithStatements.create(st)
        (Some(question), statements)
      }
      case _ => (None, List.empty)
    }
  }

  def update(q: Question)(implicit s: Session): Int =
    questions.filter(_.id === q.id.get).update(q)

  def update(q: JsonQuestion)(implicit s: Session): Int = {
    update(convertToQuestion(q))
  }

  def find(id: Long)(implicit s: Session): Option[Question] =
    questions.filter(_.id === id).firstOption

  def delete(id: Long)(implicit s: Session): Int = {
    find(id) match {
      case Some(ques) => {
        for (
          qws <- QuestionsWithStatements.findByQuestionId(ques.id.get)
        ) QuestionsWithStatements.delete(qws.id.get)
        questions.filter(_.id === id).delete
      }
      case None => 0
    }
  }

  def findJsonQuestion(id: Long)(implicit s: Session): Option[JsonQuestion] =
    questions.filter(_.id === id).firstOption match {
      case Some(q) => Some(convertToJsonQuestion(q, None))
      case _ => None
    }

  def all(implicit s: Session) = {
    val query = for {
      q <- questions
    } yield q
    val jquestions = for {
      question <- query.list
    } yield convertToJsonQuestion(question, None)
    jquestions
  }

  def allWithStatements(implicit s: Session): List[JsonQuestion] = {
    all.map { q =>
      Questions.findWithStatements(q.id.get)
    }
  }

  def findWithStatements(id: Long)(implicit s: Session): JsonQuestion = {
    var query = for {
      qws <- questionsWithStatements if qws.questionId === id
      s <- qws.statements
    } yield s
    convertToJsonQuestion(find(id).get, Some(query.list))
  }

  def findByStatement(statementId: Long)(implicit s: Session): (List[Question], Option[Statement]) = {
    var query = for {
      qws <- questionsWithStatements if qws.statementId === statementId
      q <- qws.questions
    } yield q
    (query.list, Statements.find(statementId))
  }

  def convertToQuestion(q: JsonQuestion): Question = {
    val p = q.picture.map(pic =>
      Some(Base64.decodeBase64(pic))
    ).getOrElse(
      None
    )
    Question(q.id, q.text, p, q.typeId, q.answer)
  }

  def convertToJsonQuestion(q: Question, l: Option[List[Statement]]): JsonQuestion = {
    val p = q.picture.map(pic =>
      Some(Base64.encodeBase64String(pic))
    ).getOrElse(
      None
    )
    JsonQuestion(q.id, q.text, p, q.typeId, q.answer, l)
  }

}
