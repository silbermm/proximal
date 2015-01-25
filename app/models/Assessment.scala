package models

import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
import scala.slick.SlickException
import scala.slick.lifted.ProvenShape
import play.api.Play.current
import play.api.Logger

case class Assesment(id: Option[Long], startDate: Long, endDate: Option[Long])
case class AssesmentWithQuestionsAndScores(id: Long, 
                                           startDate: Long,
                                           endDate: Option[Long] 
                                           
                                           )

class Assesments(tag: Tag) extends Table[Assesment](tag, "assesments"){
  def id = column[Long]("id", O.PrimaryKey,O.AutoInc)
  def startDate = column[Long]("startDate")
  def endDate = column[Option[Long]]("endDate")

  def * = (id.?, startDate, endDate) <> (Assesment.tupled,Assesment.unapply _)

}

object Assesments {
  
  lazy val assesments = TableQuery[Assesments]

  def create(qs: Assesment)(implicit s: Session) : Assesment =
    (assesments returning assesments.map(_.id) into ((assesment,id) => assesment.copy(Some(id)))) += qs

  def delete(id: Long)(implicit s: Session) : Int =
    assesments.filter(_.id === id).delete

  def find(id: Long)(implicit s: Session) =
    assesments.filter(_.id === id).firstOption
}