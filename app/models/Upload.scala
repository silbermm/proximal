package models

import play.api.db.slick.Config.driver.simple._

import scala.slick.lifted.Tag

case class Upload(id: Option[Long], content: String, contentType: Option[String], filename: Option[String])

class Uploads(tag: Tag) extends Table[Upload](tag, "uploads") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def content = column[String]("content", O.DBType("Text"))
  def contentType = column[Option[String]]("content_type")
  def filename = column[Option[String]]("filename")
  def * = (id.?, content, contentType, filename) <> (Upload.tupled, Upload.unapply _)
}

object Uploads {

  lazy val uploads = TableQuery[Uploads]
  lazy val questionUploads = QuestionUploads.questionUploads

  def create(u: Upload)(implicit s: Session): Upload =
    (uploads returning uploads.map(_.id) into ((upload, id) => upload.copy(Some(id)))) += u

  def delete(u: Upload)(implicit s: Session): Int =
    uploads.filter(_.id === u.id.get).delete

  def all(implicit s: Session) =
    uploads.list

  def find(id: Long)(implicit s: Session) =
    uploads.filter(_.id === id).firstOption

  def findByQuestion(questionId: Long)(implicit s: Session) = {
    var query = for {
      questionUpload <- questionUploads if questionUpload.questionId === questionId
      upload <- questionUpload.upload
    } yield upload
    query.firstOption
  }
}
