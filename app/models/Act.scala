package models

import play.api.db.slick.Config.driver.simple._

import scala.slick.lifted.Tag

case class Act(id: Option[Long], actType: String, action: Option[String], progress: Option[String], resourceId: Option[Long])

case class ActWithScore(id: Option[Long], actType: String, action: Option[String], progress: Option[String], resourceId: Option[Long], score: Option[Score])

class Acts(tag: Tag) extends Table[Act](tag, "act") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def actType = column[String]("type")
  def action = column[Option[String]]("action")
  def progress = column[Option[String]]("progress")
  def resourceId = column[Option[Long]]("resourceId")

  def * = (id.?, actType, action, progress, resourceId) <> (Act.tupled, Act.unapply _)
}

object Acts {
  lazy val acts = TableQuery[Acts]
  lazy val scores = Scores.scores

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

  def findWithScore(id: Long)(implicit s: Session) = {

    def withScore(id: Long, score: Option[Score]): Option[ActWithScore] = {
      find(id) match {
        case Some(ac) => Some(ActWithScore(ac.id, ac.actType, ac.action, ac.progress, ac.resourceId, score))
        case _ => None
      }
    }

    val query = for {
      score <- scores if score.actId === id
      act <- score.act
    } yield score

    query.firstOption match {
      case Some(s) => withScore(id, Some(s))
      case _ => withScore(id, None)
    }
  }
}
