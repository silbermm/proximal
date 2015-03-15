package models

import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
import scala.slick.SlickException
import scala.slick.lifted.ProvenShape
import play.api.Play.current
import play.api.Logger

case class ActivitySet(id: Option[Long], activityId: Long, setId: Long)

class ActivitySets(tag: Tag) extends Table[ActivitySet](tag, "activity_sets") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def activityId = column[Long]("activity_id")
  def setId = column[Long]("set_id")

  def * = (id.?, activityId, setId) <> (ActivitySet.tupled, ActivitySet.unapply _)

  def activity = foreignKey("activityset_activity_fk", activityId, Activities.activities)(_.id)
  def set = foreignKey("activityset_set_fk", setId, Sets.sets)(_.id)
}

object ActivitySets {
  lazy val activitySets = TableQuery[ActivitySets]

  def create(a: ActivitySet)(implicit s: Session): ActivitySet =
    (activitySets returning activitySets.map(_.id) into ((activity, id) => activity.copy(Some(id)))) += a

  def find(id: Long)(implicit s: Session) =
    activitySets.filter(_.id === id).firstOption

}
