package services

import akka.actor.Actor
import models._
import play.api.Play.current
import play.api.db.slick.DB

case class CreateAttempt(attempt: Attempt)
case class DeleteAttempt(attempt: Attempt)

class AttemptActor extends Actor {

  def receive = {
    case CreateAttempt(attempt) => {
      try {
        sender ! AttemptActor.createAttempt(attempt)
      } catch {
        case e: Throwable => sender ! e
      }
    }
    case DeleteAttempt(attempt) => {
      try {
        sender ! AttemptActor.deleteAttempt(attempt)
      } catch {
        case e: Throwable => sender ! e
      }
    }
  }

}

object AttemptActor {

  def createAttempt(attempt: Attempt) = {
    DB.withSession { implicit s =>
      Attempts.create(attempt)
    }
  }

  def deleteAttempt(attempt: Attempt) = {
    DB.withSession { implicit s =>
      Attempts.delete(attempt)
    }
  }
}
