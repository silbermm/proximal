package models

import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
import scala.slick.SlickException
import scala.slick.lifted.ProvenShape
import play.api.Play.current
import play.api.Logger

case class Set(id: Option[Long], title: Option[String], description: Option[String])

class Sets(tag: Tag) extends Table[Set](tag, "sets") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def title = column[Option[String]]("title")
  def description = column[Option[String]]("description")

  def * = (id.?, title, description) <> (Set.tupled, Set.unapply _)

}

object Sets {
  lazy val sets = TableQuery[Sets]

  def create(a: Set)(implicit s: Session): Set = {
    (sets returning sets.map(_.id) into ((aset, id) => aset.copy(Some(id)))) += a
  }

  def find(id: Long)(implicit s: Session) =
    sets.filter(_.id === id).firstOption

}

