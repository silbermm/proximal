package models

import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
import scala.slick.SlickException
import scala.slick.lifted.ProvenShape
import play.api.Play.current
import play.api.Logger

case class AssessmentHistory(id: Option[Long], assessmentId: Long, activityId: Long, scoreId: Long)

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
    history.filter(_.assessmentId === assessmentId).list

}
