package models

import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
import scala.slick.SlickException
import scala.slick.lifted.ProvenShape
import play.api.Logger

case class ActivityAct(id: Option[Long], activityId: Long, actId: Long)

class ActivityActs(tag: Tag) extends Table[ActivityAct](tag, "activity_acts") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def activityId = column[Long]("activity_id")
  def actId = column[Long]("act_id")

  def * = (id.?, activityId, actId) <> (ActivityAct.tupled, ActivityAct.unapply _)

  def activity = foreignKey("activityact_activity_fk", activityId, Activities.activities)(_.id)
  def act = foreignKey("activityact_act_fk", actId, Acts.acts)(_.id)
}

object ActivityActs {
  lazy val activityActs = TableQuery[ActivityActs]

  def create(a: ActivityAct)(implicit s: Session): ActivityAct =
    (activityActs returning activityActs.map(_.id) into ((act, id) => act.copy(Some(id)))) += a

  def find(id: Long)(implicit s: Session) =
    activityActs.filter(_.id === id).firstOption

  def all(implicit s: Session) =
    activityActs.list

  def delete(a: ActivityAct)(implicit s: Session): Int =
    activityActs.filter(_.id === a.id.get).delete
}
