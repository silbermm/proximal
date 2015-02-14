package models

import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
import scala.slick.SlickException
import scala.slick.lifted.ProvenShape
import play.api.Logger

case class Act(id: Option[Long], actType: String, action: Option[String], resourceId: Option[Long])

class Acts(tag: Tag) extends Table[Act](tag, "act") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def actType = column[String]("type")
  def action = column[Option[String]]("action")
  def resourceId = column[Option[Long]]("resourceId")

  def * = (id.?, actType, action, resourceId) <> (Act.tupled, Act.unapply _)
}

object Acts {
  lazy val acts = TableQuery[Acts]

  def create(a: Act)(implicit s: Session): Act =
    (acts returning acts.map(_.id) into ((act, id) => act.copy(Some(id)))) += a

  def update(a: Act)(implicit s: Session): Act = {
    acts.filter(_.id === a.id.get).update(a)
    find(a.id.get).get
  }

  def delete(a: Act)(implicit s: Session): Int =
    acts.filter(_.id === a.id.get).delete

  def all(implicit s: Session) =
    acts.list

  def find(id: Long)(implicit s: Session) =
    acts.filter(_.id === id).firstOption

}
