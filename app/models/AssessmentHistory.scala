package models

import play.api.db.slick.Config.driver.simple._

import scala.slick.lifted.Tag

case class AssessmentHistory(id: Option[Long], assessmentId: Long, activityId: Long, scoreId: Long)
case class AssessmentHistoryWithScore(id: Option[Long], assessmentId: Long, activityId: Long, scoreId: Long, score: Option[Score])

class AssessmentHistories(tag: Tag) extends Table[AssessmentHistory](tag, "assessment_history") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def assessmentId = column[Long]("assessment_id")
  def activityId = column[Long]("activity_id")
  def scoreId = column[Long]("score_id")

  def * = (id.?, assessmentId, activityId, scoreId) <> (AssessmentHistory.tupled, AssessmentHistory.unapply _)

  def assessment = foreignKey("history_assessment_fk", assessmentId, Assesments.assesments)(_.id)
  def activity = foreignKey("history_activity_fk", activityId, Activities.activities)(_.id)
  def score = foreignKey("history_score_fk", scoreId, Scores.scores)(_.id)
}

object AssessmentHistories {

  lazy val history = TableQuery[AssessmentHistories]

  def create(ah: AssessmentHistory)(implicit s: Session) =
    (history returning history.map(_.id) into ((h, id) => h.copy(Some(id)))) += ah

  def findByAssessment(assessmentId: Long)(implicit s: Session) =
    history.filter(_.assessmentId === assessmentId).sortBy(_.id.asc).list

  def findByAssessmentWithScore(assessmentId: Long)(implicit s: Session) = {
    val q = for {
      h <- history if h.assessmentId === assessmentId
      score <- h.score
    } yield (h, score)

    q.list.map(x => {
      //val h = history.filter(_.assessmentId === x._1.assessmentId).first
      AssessmentHistoryWithScore(x._1.id, x._1.assessmentId, x._1.activityId, x._1.scoreId, Some(x._2))
    })
  }

}
