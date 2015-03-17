package services

import play.api.libs.concurrent.Akka
import play.api.Logger
import play.api.db.slick.DB
import play.api.Play.current
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import models._

case class CreateHomeworkActivity(statementIds: List[Long], activity: Activity, homework: Homework, acts: List[Act])
case class ListHomework(studentId: Long)
case class DeleteHomework(uid: Long, homeworkId: Long)

case class CreateActivity(activity: Activity, statementIds: List[Long])
case class ListActivities(uid: Long)

class ActivityActor extends Actor {

  def receive = {
    case ca: CreateActivity => {
      try {
        val newActivity = ActivityActor.createActivity(ca)
        sender ! newActivity
      } catch {
        case e: Throwable => {
          sender ! akka.actor.Status.Failure(e)
          throw e
        }
      }
    }
    case la: ListActivities => {
      try {
        val activities = ActivityActor.listActivities(la.uid)
        sender ! activities
      } catch {
        case e: Throwable => {
          sender ! akka.actor.Status.Failure(e)
          throw e
        }
      }
    }
    case cha: CreateHomeworkActivity => {
      val newActivity = ActivityActor.createHomework(cha)
      sender ! newActivity
    }
    case student: ListHomework => {
      val allHomework = ActivityActor.listHomework(student.studentId)
      sender ! allHomework
    }
    case activity: DeleteHomework => {
      val deleted = ActivityActor.deleteHomework(activity.uid, activity.homeworkId)
      sender ! deleted
    }
    case _ => sender ! None
  }

}

object ActivityActor {

  def deleteHomework(uid: Long, homeworkId: Long) = {
    DB.withSession { implicit s =>
      Homeworks.find(homeworkId) match {
        case Some(h) => {
          PersonService.isChildOf(uid, h.studentId.get, c => {
            Activities.find(h.activityId.get) match {
              case Some(a) => {
                ActivityActs.deleteByActivity(a.id.get)
                ActivityStatements.deleteByActivity(a.id.get)
                Homeworks.delete(h)
                var deleted = Activities.delete(a)
                Some(deleted)
              }
              case _ => None
            }
          })
        }
        case _ => 0
      }
    }
  }

  def createActivity(ca: CreateActivity): Option[Activity] = {
    DB.withSession { implicit s =>
      val newActivity = Activities.create(ca.activity)
      val activityStatements = ca.statementIds map (sid =>
        ActivityStatements.create(ActivityStatement(None, newActivity.id.get, sid))
      )
      Some(newActivity)
    }
  }

  def listActivities(uid: Long): List[Activity] = {
    DB.withSession { implicit s =>
      People.findPersonByUid(uid) match {
        case Some(person) => Activities.allByPerson(person.id.get)
        case _ => List.empty
      }
    }
  }

  def createHomework(homeworkActivity: CreateHomeworkActivity): Option[CreateHomeworkActivity] = {
    DB.withSession { implicit s =>
      homeworkActivity.activity.id match {
        case Some(identifier) => {
          //don't create a new activity, it already exists
          //TODO: Update said activity
          //TODO: Update Acts as well
          val activityStatements = homeworkActivity.statementIds map (ActivityStatement(None, identifier, _))
          val createdActivityStatements: List[Long] = createActivityStatements(activityStatements) map (_.statementId)
          val newHomework = Homeworks.create(homeworkActivity.homework)
          Some(CreateHomeworkActivity(createdActivityStatements, homeworkActivity.activity, newHomework, homeworkActivity.acts))
        }
        case None => {
          //create a new activity as well as the homework
          val newActivity = Activities.create(homeworkActivity.activity)
          val acts = handleActs(homeworkActivity.acts)
          createActivityActs(newActivity, acts)
          val activityStatements = homeworkActivity.statementIds map (ActivityStatement(None, newActivity.id.get, _))
          val createdActivityStatements: List[Long] = createActivityStatements(activityStatements) map (_.statementId)
          val homework = homeworkActivity.homework.copy(activityId = newActivity.id)
          val newHomework = Homeworks.create(homework)
          Some(CreateHomeworkActivity(createdActivityStatements, newActivity, newHomework, acts))
        }
      }
    }
  }

  def listHomework(studentId: Long): List[HomeworkActivityActs] = {
    DB.withSession { implicit s =>
      Homeworks.findByStudentWithActivitiesAndActs(studentId)
    }
  }

  def handleActs(acts: List[Act]): List[Act] = {
    DB.withSession { implicit s =>
      val actsToCreate = for { act <- acts if act.id.isEmpty } yield act
      val actsToUpdate = for { act <- acts if !act.id.isEmpty } yield act
      val createdActs = for { act <- actsToCreate } yield Acts.create(act)
      val updatedActs = for { act <- actsToUpdate } yield Acts.update(act)
      createdActs ::: updatedActs
    }
  }

  def createActivityActs(activity: Activity, acts: List[Act]) = {
    DB.withSession { implicit s =>
      for {
        act <- acts
      } ActivityActs.create(ActivityAct(None, activity.id.get, act.id.get))
    }
  }

  def createActivityStatements(ast: List[ActivityStatement]) = {
    DB.withSession { implicit s =>
      for {
        actStatements <- ast
      } yield (ActivityStatements.create(actStatements))
    }
  }
}

