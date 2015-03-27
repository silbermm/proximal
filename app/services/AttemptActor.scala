package services

import play.api.libs.concurrent.Akka
import play.api.Logger
import services._
import models._
import play.api.db.slick.DB
import play.api.Play.current
import scala.util.Random
import scala.compat.Platform
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import play.api.libs.concurrent.Execution.Implicits.defaultContext

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
