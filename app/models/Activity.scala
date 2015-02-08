package models

import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
import scala.slick.SlickException
import scala.slick.lifted.ProvenShape
import play.api.Play.current
import play.api.Logger

case class Activity(id: Option[Long],
  creator: Option[String],
  date: Long,
  description: Option[String],
  rights: Option[String],
  source: Option[String],
  subject: Option[String],
  title: Option[String],
  category: Option[String])

case class ActivityWithStatements(id: Option[Long],
  creator: Option[String],
  date: Long,
  description: Option[String],
  rights: Option[String],
  source: Option[String],
  subject: Option[String],
  title: Option[String],
  category: Option[String],
  statements: List[Statement])

class Activities(tag: Tag) extends Table[Activity](tag, "activities") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def creator = column[Option[String]]("creator")
  def date = column[Long]("date")
  def description = column[Option[String]]("description")
  def rights = column[Option[String]]("rights")
  def source = column[Option[String]]("source")
  def subject = column[Option[String]]("subject")
  def title = column[Option[String]]("title")
  def category = column[Option[String]]("category")

  def * = (id.?, creator, date, description, rights, source, subject, title, category) <> (Activity.tupled, Activity.unapply _)

}

object Activities {
  lazy val activities = TableQuery[Activities]
  lazy val activityStatements = ActivityStatements.activityStatements

  def create(a: Activity)(implicit s: Session): Activity =
    (activities returning activities.map(_.id) into ((activity, id) => activity.copy(Some(id)))) += a

  def delete(a: Activity)(implicit s: Session): Int =
    activities.filter(_.id === a.id.get).delete

  def all(implicit s: Session) =
    activities.list

  def find(id: Long)(implicit s: Session) =
    activities.filter(_.id === id).firstOption

  def findWithStatements(id: Long)(implicit s: Session): Option[ActivityWithStatements] = {
    val query = for {
      as <- activityStatements if as.activityId === id
      a <- as.statement
    } yield a

    find(id) match {
      case Some(act) => Some(ActivityWithStatements(act.id, act.creator, act.date, act.description, act.rights, act.source, act.subject, act.title, act.category, query.list))
      case _ => None
    }
  }
}

