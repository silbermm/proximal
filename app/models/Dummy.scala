package models

import java.util.Date
import java.sql.Timestamp
import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
import scala.slick.SlickException
import scala.slick.lifted.ProvenShape
import play.api.Play.current
import play.api.Logger

case class Dummy(id: Option[Long], name: String, description: String)

class Dummies(tag: Tag) extends Table[Dummy](tag, "dummies") {
  
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def description = column[String]("description")

  def * = (id.?, name, description) <> (Dummy.tupled, Dummy.unapply _)

}

object Dummies {
  val dummies = TableQuery[Dummies] 

  def list(implicit s: Session) =
    dummies.list

  def insert(dummy: Dummy)(implicit s: Session) = 
    (dummies returning dummies.map(_.id) into ((d,id) => d.copy(id=Some(id)))) += dummy 

  def update(id: Long, dummy: Dummy)(implicit s: Session) =
    dummies.filter(_.id === id).update(dummy)

  def delete(dummy: Dummy)(implicit s: Session) =
    dummies.filter(_.id === dummy.id.get).delete

  def find(id: Long)(implicit s: Session) = 
    dummies.filter(_.id === id).firstOption

}
