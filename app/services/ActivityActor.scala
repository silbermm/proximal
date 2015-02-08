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

case class CreateHomeworkActivity(activity: Activity, homework: Homework)

class ActivityActor extends Actor {

  def receive = {
    case cha: CreateHomeworkActivity => {
      val newActivity = createHomework(cha)
      sender ! newActivity
    }
    case _ => sender ! None
  }

  def createHomework(homeworkActivity: CreateHomeworkActivity): Option[CreateHomeworkActivity] = {
    DB.withSession { implicit s =>
      homeworkActivity.activity.id match {
        case Some(identifier) => {
          //don't create a new activity, it already exists
          val newHomework = Homeworks.create(homeworkActivity.homework)
          Some(CreateHomeworkActivity(homeworkActivity.activity, newHomework))
        }
        case None => {
          //create a new activity as well as the homework
          val newActivity = Activities.create(homeworkActivity.activity)
          val homework = homeworkActivity.homework.copy(activityId = newActivity.id)
          val newHomework = Homeworks.create(homework)
          Some(CreateHomeworkActivity(newActivity, newHomework))
        }
      }
    }
  }

}
