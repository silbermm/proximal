package services

import akka.actor.Actor
import models._
import play.api.Play.current
import play.api.db.slick.DB

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
