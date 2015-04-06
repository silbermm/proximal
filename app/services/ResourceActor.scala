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

package services

import akka.actor.Actor
import models._
import play.api.Play.current
import play.api.db.slick.DB

case class CreateResource(resource: Resource, uid: Option[Long])
case class DeleteResource(resource: Resource)
case class UpdateResource(resource: Resource)
case class GetResource(resourceId: Long)
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
    case GetResource(resourceId) => {
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
        val person = People.findPersonByUid(uid.get);
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
