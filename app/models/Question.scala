package models

import java.util.Date
import java.sql.Timestamp
import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
import scala.slick.SlickException
import scala.slick.lifted.ProvenShape
import play.api.Play.current
import play.api.Logger

case class Question(id: Option[Long], text: String, picture: Option[Array[Byte]], typeId: Option[Long])
class Questions(tag: Tag) extends Table[Question](tag,"questions"){
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def text = column[String]("text", O.DBType("Text"))
  def picture = column[Option[Array[Byte]]]("picture")
  def typeId = column[Option[Long]]("type_id")

  def * = (id.?, text, picture, typeId) <> (Question.tupled, Question.unapply _)
}

object Questions {
  
  lazy val questions = TableQuery[Questions]

  def create(q: Question)(implicit s: Session) = 
    (questions returning  questions.map(_.id) into ((question,id) => question.copy(Some(id)))) += q

  def update(q: Question)(implicit s: Session) = 
    questions.filter(_.id === q.id.get).update(q)

  def find(id: Long)(implicit s: Session) = 
    questions.filter(_.id === id).firstOption

  def all(implicit s: Session) = 
    questions.list

}
