package models

import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
import scala.slick.SlickException
import scala.slick.lifted.ProvenShape
import play.api.Logger

case class QuestionUpload(id: Option[Long], questionId: Long, uploadId: Long)

class QuestionUploads(tag: Tag) extends Table[QuestionUpload](tag, "question_uploads") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def questionId = column[Long]("question_id")
  def uploadId = column[Long]("upload_id")

  def * = (id.?, questionId, uploadId) <> (QuestionUpload.tupled, QuestionUpload.unapply _)

  def question = foreignKey("questionupload_question_fk", questionId, Questions.questions)(_.id)
  def upload = foreignKey("questionupload_upload_fk", uploadId, Uploads.uploads)(_.id)
}

object QuestionUploads {

  lazy val questionUploads = TableQuery[QuestionUploads]

  def create(q: QuestionUpload)(implicit s: Session): QuestionUpload =
    (questionUploads returning questionUploads.map(_.id) into ((upload, id) => upload.copy(Some(id)))) += q

  def delete(q: QuestionUpload)(implicit s: Session): Int =
    questionUploads.filter(_.id === q.id.get).delete

  def all(implicit s: Session): List[QuestionUpload] =
    questionUploads.list

  def find(q: Long)(implicit s: Session) =
    questionUploads.filter(_.id === q).firstOption

  def findByQuestion(q: Long)(implicit s: Session) = {
    var query = for {
      upload <- questionUploads if upload.questionId === q
      question <- upload.question
    } yield upload

    query.firstOption
  }

}
