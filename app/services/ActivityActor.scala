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

class ActivityActor extends Actor {

  def receive = {
    case cha: CreateHomeworkActivity => {
      val newActivity = ActivityActor.createHomework(cha)
      sender ! newActivity
    }
    case _ => sender ! None
  }

}

object ActivityActor {
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
          createActivityActs(homeworkActivity.activity, homeworkActivity.acts)
          val activityStatements = homeworkActivity.statementIds map (ActivityStatement(None, newActivity.id.get, _))
          val createdActivityStatements: List[Long] = createActivityStatements(activityStatements) map (_.statementId)
          val homework = homeworkActivity.homework.copy(activityId = newActivity.id)
          val newHomework = Homeworks.create(homework)
          Some(CreateHomeworkActivity(createdActivityStatements, newActivity, newHomework, acts))
        }
      }
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

