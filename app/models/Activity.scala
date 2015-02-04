package models

import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
import scala.slick.SlickException
import scala.slick.lifted.ProvenShape
import play.api.Play.current
import play.api.Logger

case class Activity(id: Option[Long], 
                    creator: String, 
                    date: Long, 
                    description: Option[String], 
                    rights: Option[String], 
                    source: Option[String], 
                    subject: Option[String], 
                    title: Option[String],
                    category: Option[String])

class Activities(tag: Tag) extends Table[Activity](tag, "activities"){
  def id = column[Long]("id", O.PrimaryKey,O.AutoInc)
  def creator = column[String]("creator")
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
}

