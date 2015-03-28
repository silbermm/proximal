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

case class Question(id: Option[Long], text: String, typeId: Option[Long], answer: Option[String], resourceId: Option[Long])

case class JsonQuestion(id: Option[Long], text: String, pictures: Option[List[Upload]], typeId: Option[Long], answer: Option[String], resourceId: Option[Long], statements: Option[List[Statement]])

class Questions(tag: Tag) extends Table[Question](tag, "questions") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def text = column[String]("text", O.DBType("Text"))
  def typeId = column[Option[Long]]("type_id")
  def answer = column[Option[String]]("answer", O.DBType("Text"))
  def resourceId = column[Option[Long]]("resource_id")

  def * = (id.?, text, typeId, answer, resourceId) <> (Question.tupled, Question.unapply _)

  def res = foreignKey("questions_resource_fk", resourceId, Resources.resources)(_.id)
}

object Questions {

  lazy val questions = TableQuery[Questions]
  lazy val questionUploads = QuestionUploads.questionUploads
  lazy val questionsWithStatements = QuestionsWithStatements.qWithS

  lazy val resources = Resources.resources

  def create(q: Question)(implicit s: Session): Question =
    (questions returning questions.map(_.id) into ((question, id) => question.copy(Some(id)))) += q

  def create(q: JsonQuestion)(implicit s: Session): JsonQuestion = {
    val question = create(convertToQuestion(q))
    convertToJsonQuestion(question, q.pictures, q.statements)
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

        // any pictures out there?
        QuestionUploads.findByQuestion(ques.id.get) map (QuestionUploads.delete(_))

        var deleted = questions.filter(_.id === id).delete
        ques.resourceId map (Resources.delete(_))
        deleted
      }
      case None => 0
    }
  }

  def findByResourceId(resourceId: Long)(implicit s: Session): Option[Question] =
    questions.filter(_.resourceId === resourceId).firstOption

  def findJsonQuestion(id: Long)(implicit s: Session): Option[JsonQuestion] =
    questions.filter(_.id === id).firstOption match {
      case Some(q) => Some(convertToJsonQuestion(q, None, None))
      case _ => None
    }

  def all(implicit s: Session) = {
    val query = for {
      q <- questions
    } yield q
    val jquestions = for {
      question <- query.list
    } yield convertToJsonQuestion(question, None, None)
    jquestions
  }

  def allWithAResourceId(implicit s: Session) = {
    val query = for {
      ques <- questions if ques.resourceId.isDefined
    } yield ques

    query.list
  }

  def allWithResource(implicit s: Session) = {
    val query = for {
      ques <- questions if ques.resourceId.isDefined
      res <- resources if res.id === ques.resourceId
    } yield (res, ques)

    query.list map { tup =>
      ResourceWithQuestion(tup._1.id,
        tup._1.title,
        tup._1.description,
        tup._1.category,
        tup._1.creator,
        tup._1.createdOn,
        Some(tup._2))
    }
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
    convertToJsonQuestion(find(id).get, None, Some(query.list))
  }

  def findByStatement(statementId: Long)(implicit s: Session): (List[Question], Option[Statement]) = {
    var query = for {
      qws <- questionsWithStatements if qws.statementId === statementId
      q <- qws.questions
    } yield q
    (query.list, Statements.find(statementId))
  }

  def convertToQuestion(jsonQuestion: JsonQuestion): Question = {
    Question(jsonQuestion.id, jsonQuestion.text, jsonQuestion.typeId, jsonQuestion.answer, jsonQuestion.resourceId)
  }

  def convertToJsonQuestion(q: Question, uploads: Option[List[Upload]], l: Option[List[Statement]]): JsonQuestion = {
    /*val p = q.picture.map(pic =>
      Some(Base64.encodeBase64String(pic))
    ).getOrElse(
      None
    )*/
    JsonQuestion(q.id, q.text, uploads, q.typeId, q.answer, q.resourceId, l)
  }

}
