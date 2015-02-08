package models

import java.util.Date
import java.sql.{ Date => SqlDate }
import org.joda.time.DateTime
import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
import scala.slick.lifted.ProvenShape
import play.api.Play.current
import securesocial.core._
import securesocial.core.services._
import securesocial.core.providers.MailToken
import com.github.tototoshi.slick.PostgresJodaSupport._

class Tokens(tag: Tag) extends Table[MailToken](tag, "Token") {
  def uuid = column[String]("uuid")
  def email = column[String]("email")
  def creationTime = column[DateTime]("creationTime")
  def expirationTime = column[DateTime]("expirationTime")
  def isSignUp = column[Boolean]("isSignUp")
  def * : ProvenShape[MailToken] = {
    val shapedValue = (uuid, email, creationTime, expirationTime, isSignUp).shaped
    shapedValue.<>({
      tuple =>
        MailToken(uuid = tuple._1,
          email = tuple._2,
          creationTime = tuple._3,
          expirationTime = tuple._4,
          isSignUp = tuple._5)
    }, {
      (t: MailToken) =>
        Some {
          (t.uuid,
            t.email,
            t.creationTime,
            t.expirationTime,
            t.isSignUp)
        }
    })
  }
}

object Tokens {

  val tokens = TableQuery[Tokens]

  def findById(tokenId: String)(implicit s: Session): Option[MailToken] =
    tokens.filter(_.uuid === tokenId).firstOption

  def save(token: MailToken)(implicit s: Session): MailToken = {
    findById(token.uuid) match {
      case None => {
        tokens.insert(token)
        token
      }
      case Some(existingToken) => {
        val tokenRow = for {
          t <- tokens
          if t.uuid === existingToken.uuid
        } yield t
        val updatedToken = token.copy(uuid = existingToken.uuid)
        tokenRow.update(updatedToken)
        updatedToken
      }
    }
  }

  def delete(uuid: String)(implicit s: Session): Option[MailToken] = {
    findById(uuid) match {
      case None => None
      case Some(existing) => {
        tokens.filter(_.uuid === uuid).delete
        Some(existing)
      }
    }
  }

  def deleteExpiredTokens(currentDate: DateTime)(implicit s: Session) = {
    val q = for {
      t <- tokens
      if t.expirationTime < currentDate
    } yield t
    q.delete
  }

}
