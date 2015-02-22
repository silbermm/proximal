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

case class CreateUpload(upload: Upload)
case class DeleteUpload(upload: Upload)
case class FindUpload(uploadId: Int)
case class AllUploads()

class UploadActor extends Actor {
  def receive = {
    case upload: CreateUpload => {
      val uploaded = UploadActor.createUpload(upload.upload)
      sender ! uploaded
    }
  }
}

object UploadActor {
  def createUpload(u: Upload) = {
    DB.withSession { implicit s =>
      try {
        val up = Uploads.create(u)
        Some(up)
      } catch {
        case ex: Throwable => {
          Logger.debug(s"$ex")
          None
        }
      }
    }
  }
}
