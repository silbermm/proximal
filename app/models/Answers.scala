package models

import org.apache.commons.codec.binary.Base64
import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
import scala.slick.SlickException
import scala.slick.lifted.ProvenShape
import play.api.Play.current
import play.api.Logger

case class Answer(id: Option[Long], text: String)

class Answers(tag: Tag) extends Table[Answer](tag,"answers"){
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def text = column[String]("text", O.DBType("Text"))

  def * = (id.?, text) <> (Answer.tupled, Answer.unapply _)
}

object Answers {
  
  lazy val answers = TableQuery[Answers]

  def create(a: Answer)(implicit s: Session) =
    (answers returning answers.map(_.id) into ((answer,id) => answer.copy(Some(id)))) += a

  def find(id: Long)(implicit s: Session) =
    answers.filter(_.id === id).firstOption

  def update(a: Answer)(implicit s: Session) =
    answers.filter(_.id === a.id.get).update(a)

  def delete(a: Answer)(implicit s: Session) =
    answers.filter(_.id === a.id.get).delete
}

