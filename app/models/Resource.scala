package models

import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
import scala.slick.SlickException
import scala.slick.lifted.ProvenShape
import play.api.Logger

case class Resource(id: Option[Long],
  title: Option[String],
  description: Option[String],
  category: Option[String],
  creator: Option[Long],
  createdOn: Option[Long])

case class ResourceWithQuestion(id: Option[Long],
  title: Option[String],
  description: Option[String],
  category: Option[String],
  creator: Option[Long],
  createdOn: Option[Long],
  question: Option[Question])

class Resources(tag: Tag) extends Table[Resource](tag, "resources") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def title = column[Option[String]]("title")
  def description = column[Option[String]]("description", O.DBType("Text"))
  def category = column[Option[String]]("type")
  def creator = column[Option[Long]]("creator")
  def createdOn = column[Option[Long]]("created_on")

  def * = (id.?, title, description, category, creator, createdOn) <> (Resource.tupled, Resource.unapply _)

  def person = foreignKey("resources_person_fk", creator, People.people)(_.id)

}

object Resources {
  lazy val resources = TableQuery[Resources]
  lazy val questions = Questions.questions

  def create(r: Resource)(implicit s: Session): Resource =
    (resources returning resources.map(_.id) into ((resource, id) => resource.copy(Some(id)))) += r

  def update(r: Resource)(implicit s: Session) =
    resources.filter(_.id === r.id.get).update(r)

  def delete(r: Long)(implicit s: Session) =
    resources.filter(_.id === r).delete

  def all(implicit s: Session) =
    resources.list

  def find(rId: Long)(implicit s: Session) =
    resources.filter(_.id === rId).firstOption

  def findWithQuestion(rId: Long)(implicit s: Session) = {
    val query = for {
      ques <- questions if ques.resourceId === rId
    } yield ques
    find(rId) match {
      case Some(resource) => {
        Some(ResourceWithQuestion(resource.id,
          resource.title,
          resource.description,
          resource.category,
          resource.creator,
          resource.createdOn,
          query.firstOption))
      }
      case None => None
    }
  }

  def allQuestions(implicit s: Session) = {
    val query = for {
      res <- resources if res.category === "question"
      ques <- questions if ques.resourceId === res.id
    } yield (res, ques)

    query.list map { tup =>
      ResourceWithQuestion(tup._1.id,
        tup._1.title,
        tup._1.description,
        tup._1.category,
        tup._1.creator,
        tup._1.createdOn,
        Some(tup._2))
    }

  }

}
