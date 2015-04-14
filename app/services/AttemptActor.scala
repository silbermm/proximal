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
      sender ! AttemptActor.createAttempt(attempt)
    }
    case DeleteAttempt(attempt) => {
      sender ! AttemptActor.deleteAttempt(attempt)
    }
    case _ => {
      val e = new Exception("Please send a message that akka can process")
      akka.actor.Status.Failure(e)
      throw e
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
