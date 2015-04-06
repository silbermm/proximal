package models

import play.api.db.slick.Config.driver.simple._

import scala.slick.lifted.Tag

case class AssesmentQuestionScore(id: Option[Long], assesmentId: Long, scoreId: Long)

class AssesmentQuestionScores(tag: Tag) extends Table[AssesmentQuestionScore](tag, "assesments_questions_scores") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def assesmentId = column[Long]("assesment_id")
  def scoreId = column[Long]("question_score_id")

  def * = (id.?, assesmentId, scoreId) <> (AssesmentQuestionScore.tupled, AssesmentQuestionScore.unapply _)

  def scores = foreignKey("assesment_question_score_fk", scoreId, Scores.scores)(_.id)
  def assesment = foreignKey("assesment_question_assesment_fk", assesmentId, Assesments.assesments)(_.id)
}

object AssesmentQuestionScores {

  val assesmentQuestionScores = TableQuery[AssesmentQuestionScores]

  def create(a: AssesmentQuestionScore)(implicit s: Session): AssesmentQuestionScore =
    (assesmentQuestionScores returning assesmentQuestionScores.map(_.id) into ((ass, id) => ass.copy(Some(id)))) += a

  def delete(id: Long)(implicit s: Session): Int = {
    assesmentQuestionScores.filter(_.id === id).delete
  }

}
