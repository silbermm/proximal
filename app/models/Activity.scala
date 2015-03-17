package models

import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
import scala.slick.SlickException
import scala.slick.lifted.ProvenShape
import play.api.Play.current
import play.api.Logger

case class Activity(id: Option[Long],
  creator: Long,
  date: Long,
  description: Option[String],
  rights: Option[String],
  source: Option[String],
  subject: Option[String],
  title: Option[String],
  category: Option[String])

case class ActivityWithStatements(id: Option[Long],
  creator: Long,
  date: Long,
  description: Option[String],
  rights: Option[String],
  source: Option[String],
  subject: Option[String],
  title: Option[String],
  category: Option[String],
  statements: List[Statement])

case class ActivityWithActs(id: Option[Long],
  creator: Long,
  date: Long,
  description: Option[String],
  rights: Option[String],
  source: Option[String],
  subject: Option[String],
  title: Option[String],
  category: Option[String],
  acts: List[Act])

case class ActivityWithStatementsAndActs(id: Option[Long],
  creator: Long,
  date: Long,
  description: Option[String],
  rights: Option[String],
  source: Option[String],
  subject: Option[String],
  title: Option[String],
  category: Option[String],
  statements: List[Statement],
  acts: List[Act])

case class ActivityWithHomeworkAndActs(id: Option[Long],
  creator: Long,
  date: Long,
  description: Option[String],
  rights: Option[String],
  source: Option[String],
  subject: Option[String],
  title: Option[String],
  category: Option[String],
  homework: Option[Homework],
  acts: List[Act])

class Activities(tag: Tag) extends Table[Activity](tag, "activities") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def creator = column[Long]("creator")
  def date = column[Long]("date")
  def description = column[Option[String]]("description")
  def rights = column[Option[String]]("rights")
  def source = column[Option[String]]("source")
  def subject = column[Option[String]]("subject")
  def title = column[Option[String]]("title")
  def category = column[Option[String]]("category")

  def * = (id.?, creator, date, description, rights, source, subject, title, category) <> (Activity.tupled, Activity.unapply _)

  def person = foreignKey("activities_person_fk", creator, People.people)(_.id)
}

object Activities {
  lazy val activities = TableQuery[Activities]
  lazy val activityStatements = ActivityStatements.activityStatements
  lazy val activityActs = ActivityActs.activityActs
  lazy val homeworks = Homeworks.homeworks

  def create(a: Activity)(implicit s: Session): Activity =
    (activities returning activities.map(_.id) into ((activity, id) => activity.copy(Some(id)))) += a

  def delete(a: Activity)(implicit s: Session): Int =
    activities.filter(_.id === a.id.get).delete

  def all(implicit s: Session) =
    activities.list

  def allByPerson(personId: Long)(implicit s: Session) =
    activities.filter(_.creator === personId).list

  /*def allWithStatements(implicit s: Session): List[ActivityWithStatements] = {
    val query = for {
      act <- activities
      as <- activityStatements if as.activityId === act.id
      a <- as.statement
    } yield (act, a)
    query.list map { tup =>
      ActivityWithStatements(tup._1.id, tup._1.creator, tup._1.date, tup._1.description, tup._1.rights, tup._1.source, tup._1.subject, tup._1.title, tup._1.category, tup._2.list)
    }
  }
  */

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

  def findWithActs(id: Long)(implicit s: Session): Option[ActivityWithActs] = {
    val query = for {
      aa <- activityActs if aa.activityId === id
      acts <- aa.act
    } yield acts

    find(id) match {
      case Some(act) => Some(ActivityWithActs(act.id, act.creator, act.date, act.description, act.rights, act.source, act.subject, act.title, act.category, query.list))
      case _ => None
    }
  }

  def findWithHomeworkAndActs(id: Long)(implicit s: Session): Option[ActivityWithHomeworkAndActs] = {
    val homeworker = for {
      h <- homeworks if h.activityId === id
    } yield h

    val acts = for {
      aa <- activityActs if aa.activityId === id
      acts <- aa.act
    } yield acts

    find(id) match {
      case Some(act) => {
        Some(ActivityWithHomeworkAndActs(
          act.id,
          act.creator,
          act.date,
          act.description,
          act.rights,
          act.source,
          act.subject,
          act.title,
          act.category,
          homeworker.firstOption,
          acts.list))
      }
      case _ => None
    }
  }

  def findWithStatementsAndActs(id: Long)(implicit s: Session): Option[ActivityWithStatementsAndActs] = {
    val statements = for {
      as <- activityStatements if as.activityId === id
      a <- as.statement
    } yield a

    val acts = for {
      aa <- activityActs if aa.activityId === id
      acts <- aa.act
    } yield acts

    find(id) match {
      case Some(act) => Some(ActivityWithStatementsAndActs(
        act.id,
        act.creator,
        act.date,
        act.description,
        act.rights,
        act.source,
        act.subject,
        act.title,
        act.category,
        statements.list,
        acts.list))
      case _ => None
    }
  }
}

