package models

import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
import scala.slick.SlickException
import scala.slick.lifted.ProvenShape
import play.api.Play.current
import play.api.Logger

case class ActivityStatement(id: Option[Long], activityId: Long, statementId: Long)

class ActivityStatements(tag: Tag) extends Table[ActivityStatement](tag, "activity_statements"){
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def activityId =column[Long]("activityId")
  def statementId = column[Long]("statementId")
  def * = (id.?, activityId, statementId) <> (ActivityStatement.tupled, ActivityStatement.unapply _)

  def activity = foreignKey("activities_activities_fk", activityId, Activities.activities)(_.id)
  def statement = foreignKey("statements_activities_fk", statementId, Statements.statements)(_.id)
}

object ActivityStatements {

  lazy val activityStatements = TableQuery[ActivityStatements]

  def create(a: ActivityStatement)(implicit s: Session) : ActivityStatement =
    (activityStatements returning activityStatements.map(_.id) into ((as,id) => as.copy(Some(id)))) += a

  def delete(a: ActivityStatement)(implicit s: Session) : Int =
    activityStatements.filter(_.id === a.id.get).delete

  def all(implicit s: Session) =
    activityStatements.list

  def find(id: Long)(implicit s: Session) =
    activityStatements.filter(_.id === id).firstOption

}
