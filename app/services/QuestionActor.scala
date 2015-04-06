package services

import akka.actor.Actor
import helpers.RolesHelper
import models._
import play.api.Logger
import play.api.Play.current
import play.api.db.slick.DB

case class CreateQuestion(question: JsonQuestion, userId: Long)

class QuestionActor extends Actor {
  def receive = {
    case question: CreateQuestion => {
      val q = QuestionActor.createQuestion(question.question, question.userId);
      sender ! q
    }
  }
}

object QuestionActor {

  def createQuestion(q: JsonQuestion, userId: Long): Option[JsonQuestion] = {
    DB.withSession { implicit s =>
      RolesHelper.isAdmin[JsonQuestion](userId, uid => {
        try {
          val question = Questions.create(q)
          question.statements.map { x =>
            for (qs <- x) QuestionsWithStatements.create(QuestionWithStatements(None, question.id.get, qs.id.get))
          }
          question.pictures.map { x =>
            for (pic <- x) QuestionUploads.create(QuestionUpload(None, question.id.get, pic.id.get))
          }
          Some(question)
        } catch {
          case ex: Throwable => {
            Logger.debug(s"$ex")
            None
          }
        }
      }
      )
    }
  }
}
