package models

import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
import scala.slick.SlickException
import scala.slick.lifted.ProvenShape
import play.api.Play.current
import play.api.Logger

case class QuestionScore(id: Option[Long], studentId: Long, questionId: Long, score: Option[Long], timestamp: Long)

case class QuestionScoreQuestion(questionScoreId: Long, 
                                 score: Option[Long],
                                 timestamp: Long,
                                 question: JsonQuestion
)

class QuestionScores(tag: Tag) extends Table[QuestionScore](tag, "question_scores") { 
  
  def id = column[Long]("id", O.PrimaryKey,O.AutoInc)
  def studentId = column[Long]("studentId")
  def questionId = column[Long]("questionId")
  def score = column[Option[Long]]("compentency") 
  def timestamp = column[Long]("timestamp")

  def * = (id.?, studentId, questionId, score, timestamp) <> (QuestionScore.tupled,QuestionScore.unapply _)

  def student = foreignKey("questionscores_student_fk", studentId, People.people)(_.id)
  def question = foreignKey("questionscores_question_fk", questionId, Questions.questions)(_.id)

}

object QuestionScores {

  lazy val questionScores = TableQuery[QuestionScores]
  lazy val questions = Questions.questions 

  def create(qs: QuestionScore)(implicit s: Session) : QuestionScore =
    (questionScores returning questionScores.map(_.id) into ((questionScores,id) => questionScores.copy(Some(id)))) += qs
 
  def update(qs: QuestionScore)(implicit s: Session) : Int = 
    questionScores.filter(_.id === qs.id.get).update(qs)

  def find(id: Long)(implicit s: Session) : Option[QuestionScore] =
    questionScores.filter(_.id === id).firstOption

  def findByStudent(studentId: Long)(implicit s: Session) =
    questionScores.filter(_.studentId === studentId).list

  def findByQuestion(questionId: Long)(implicit s: Session) = 
    questionScores.filter(_.questionId === questionId).list

  def findByQuestionAndStudent(questionId: Long, studentId: Long)(implicit s: Session) = {
    questionScores.filter( x => x.questionId === questionId && x.studentId === studentId).firstOption
  }

  def findWithQuestions(id: Long)(implicit s: Session) : QuestionScoreQuestion = {
    val query = for {
      qs <- questionScores if qs.id === id
      q <- qs.question
    } yield (qs, q)
    val resp = query.first
    QuestionScoreQuestion(id,resp._1.score, resp._1.timestamp,Questions.convertToJsonQuestion(resp._2,None))
  }

  def findByStudentWithQuestions(studentId: Long)(implicit s: Session) : List[QuestionScoreQuestion] = {
    val query = for {
      qs <- questionScores if qs.studentId === studentId
      q <- qs.question
    } yield (qs, q)
    val resp = query.list
    resp.map{ old =>
      QuestionScoreQuestion(old._1.id.get, old._1.score, old._1.timestamp, Questions.convertToJsonQuestion(old._2,None))
    }
  }

  def delete(id: Long)(implicit s: Session) : Int = 
    questionScores.filter(_.id === id).delete

  def all(implicit s: Session) : List[QuestionScore] = 
    questionScores.list

  def allWithQuestions(implicit s: Session) : List[QuestionScoreQuestion] = {
    all.map{ qs =>
      findWithQuestions(qs.id.get) 
    }
  }
}

