package models

import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
import scala.slick.SlickException
import scala.slick.lifted.ProvenShape
import play.api.Play.current
import play.api.Logger

case class AssesmentQuestionScore(id: Option[Long], questionScoreId: Long)

class AssesmentQuestionScores(tag: Tag) extends Table[AssesmentQuestionScore](tag, "assesments_questions_scores"){

  def id = column[Long]("id", O.PrimaryKey,O.AutoInc)
  def questionScoreId = column[Long]("question_score_id")

  def * = (id.?, questionScoreId) <> (AssesmentQuestionScore.tupled, AssesmentQuestionScore.unapply _)

  def questionScores = foreignKey("assesment_question_score_fk", questionScoreId, QuestionScores.questionScores)(_.id)

}

object AssesmentQuestionScores {
  
  val assesmentQuestionScores = TableQuery[AssesmentQuestionScores]
  
  def create(a: AssesmentQuestionScore)(implicit s: Session) : AssesmentQuestionScore = 
    (assesmentQuestionScores returning assesmentQuestionScores.map(_.id) into ((ass, id) => ass.copy(Some(id)))) += a

  def delete(id: Long)(implicit s: Session) : Int = {
    assesmentQuestionScores.filter(_.id === id).delete
  }

}
