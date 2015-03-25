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

case class CreateResource(resource: Resource, uid: Option[Long])
case class DeleteResource(resource: Resource)
case class UpdateResource(resource: Resource)
case class FindResource(resourceId: Long)
case class ListResources()

class ResourceActor extends Actor {
  def receive = {
    case CreateResource(resource, uid) => {
      try {
        sender ! ResourceActor.createResource(resource, uid)
      } catch {
        case e: Throwable => {
          sender ! akka.actor.Status.Failure(e)
        }
      }
    }
    case DeleteResource(resource) => {
      try {
        sender ! ResourceActor.deleteResource(resource)
      } catch {
        case e: Throwable => {
          sender ! akka.actor.Status.Failure(e)
        }
      }
    }
    case UpdateResource(resource) => {
      try {
        sender ! ResourceActor.updateResource(resource)
      } catch {
        case e: Throwable => {
          sender ! akka.actor.Status.Failure(e)
        }
      }
    }
    case FindResource(resourceId) => {
      try {
        sender ! ResourceActor.findResource(resourceId)
      } catch {
        case e: Throwable => {
          sender ! akka.actor.Status.Failure(e)
        }
      }
    }
    case ListResources => {
      try {
        sender ! ResourceActor.listResources
      } catch {
        case e: Throwable => {
          sender ! akka.actor.Status.Failure(e)
        }
      }
    }

    case _ => {
      val e = new Exception("Not Implemented!")
      sender ! akka.actor.Status.Failure(e)
      throw e
    }

  }
}

object ResourceActor {

  def createResource(resource: Resource, uid: Option[Long]): Resource = {
    DB.withSession { implicit s =>
      if (uid.isEmpty) {
        Resources.create(resource)
      } else {
        var person = People.findPersonByUid(uid.get);
        Resources.create(resource.copy(creator = person.get.id))
      }
    }
  }

  def deleteResource(resource: Resource): Int = {
    DB.withSession { implicit s =>
      Resources.delete(resource.id.get)
    }
  }

  def updateResource(resource: Resource): Int = {
    DB.withSession { implicit s =>
      Resources.update(resource)
    }
  }

  def findResource(resourceId: Long): Option[Resource] = {
    DB.withSession { implicit s =>
      Resources.find(resourceId)
    }
  }

  def listResources: List[Resource] = {
    DB.withSession { implicit s =>
      Resources.all
    }
  }

}
