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

case class UpdateScore(score: Score)
case class ListScores(studentId: Long)
case class CreateScore(score: Score)

class ScoreActor extends Actor {

  def receive = {
    case update: UpdateScore => {
      val updated = ScoreActor.updateScore(update.score)
      sender ! updated
    }
    case list: ListScores => {
      val scores = ScoreActor.listScores(list.studentId)
      sender ! scores
    }
    case create: CreateScore => {
      val score = ScoreActor.createScore(create.score)
      sender ! score
    }
  }
}

object ScoreActor {

  def createScore(score: Score) = {
    DB.withSession { implicit s =>
      Scores.create(score)
    }
  }

  def updateScore(score: Score) = {
    DB.withSession { implicit s =>
      try {
        Scores.update(score)
      } catch {
        case e: Exception => 0
      }
    }
  }

  def listScores(studentId: Long) = {
    DB.withSession { implicit s =>
      try {
        Scores.findByStudent(studentId)
      } catch {
        case e: Exception => List.empty
      }
    }

  }

}
