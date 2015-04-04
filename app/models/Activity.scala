/* Copyright 2015 Matt Silbernagel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
  category: Option[String],
  resourceId: Option[Long])

case class ActivityWithResource(id: Option[Long],
  creator: Long,
  date: Long,
  description: Option[String],
  rights: Option[String],
  source: Option[String],
  subject: Option[String],
  title: Option[String],
  category: Option[String],
  resource: Option[Resource])

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
  def resourceId = column[Option[Long]]("resource_id")

  def * = (id.?, creator, date, description, rights, source, subject, title, category, resourceId) <> (Activity.tupled, Activity.unapply _)

  def person = foreignKey("activities_person_fk", creator, People.people)(_.id)
  def resource = foreignKey("activities_resource_fk", resourceId, Resources.resources)(_.id)
}

object Activities {
  lazy val activities = TableQuery[Activities]
  lazy val activityStatements = ActivityStatements.activityStatements
  lazy val activityActs = ActivityActs.activityActs
  lazy val homeworks = Homeworks.homeworks
  lazy val attempts = Attempts.attempts
  lazy val statementLevels = StatementLevels.statement_levels
  lazy val people = People.people

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

  def findWithResource(id: Long)(implicit s: Session): Option[ActivityWithResource] = {
    val query = for {
      activ <- activities if activ.id === id
      resource <- activ.resource
    } yield resource

    find(id) match {
      case Some(activity) => {
        Some(ActivityWithResource(
          activity.id,
          activity.creator,
          activity.date,
          activity.description,
          activity.rights,
          activity.source,
          activity.subject,
          activity.title,
          activity.category,
          query.firstOption))
      }
      case _ => None
    }

  }

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

  def includesStatement(activityId: Long, statementId: Long)(implicit s: Session): Boolean = {
    findWithStatements(activityId) map (aWs => {
      val vv = aWs.statements.filter(aa => {
        statementId == aa.id.get
      })
      vv.isEmpty
    }) getOrElse false
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

  def filterByStandardLevelCategory(childId: Long, standardId: Long, category: String)(implicit s: Session): List[Activity] = {

    val queryActivities = for {
      a <- activities if a.category === category
      activitySt <- activityStatements if activitySt.activityId === a.id
      statements <- activitySt.statement if statements.standardId === standardId
      person <- people if person.id === childId
      edLevel <- person.educationLevel
      levels <- statementLevels if levels.statementId === statements.id && levels.educationLevelId === edLevel.id
    } yield a

    queryActivities.list
  }
}

