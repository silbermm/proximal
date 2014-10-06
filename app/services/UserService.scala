package services

import play.api.Logger
import securesocial.core._
import securesocial.core.providers.{ UsernamePasswordProvider, MailToken }
import scala.concurrent.Future
import securesocial.core.services.{ UserService, SaveMode }
import models._
import play.api.db.slick.DB
import play.api.Play.current
import org.joda.time.DateTime

class SecureUserService extends UserService[SecureUser]  {
  
  def deleteExpiredTokens(): Unit ={
    DB.withSession{ implicit s=>
      Tokens.deleteExpiredTokens(new DateTime)
    }
  }

  def deleteToken(uuid: String): Future[Option[MailToken]] = {
    DB.withSession{ implicit s=>
      val t = Tokens.delete(uuid)
      Future.successful(t)
    }
  }
  
  def find(providerId: String,userId: String): Future[Option[BasicProfile]] = {
    DB.withSession{ implicit s => 
      val u : Option[SecureUser] = SecureUsers.findByProviderIdAndUserId(providerId,userId)
      u match {
        case _: SecureUser => Future.successful(Some(ProfileFromUser(u)))
        case _ => Future.successful(None)
      }
    }
  }
 
  def findByEmailAndProvider(email: String,providerId: String): Future[Option[BasicProfile]] = {
    DB.withSession{ implicit s => 
      val u: Option[SecureUser] = SecureUsers.findByEmailAndProvider(email,providerId)
      u match {
        case _: SecureUser => Future.successful(Some(ProfileFromUser(u)))
        case _ => Future.successful(None)
      }
    }
  }
  
  def findToken(token: String): Future[Option[MailToken]] = {
    DB.withSession{ implicit s =>
      Future.successful(Tokens.findById(token))
    }
  }

  def link(current: SecureUser,to: BasicProfile): Future[models.SecureUser] = ???
  
  def passwordInfoFor(user: models.SecureUser): Future[Option[PasswordInfo]] = {
    DB.withSession{ implicit s => 
      SecureUsers.findById(user.uid.get) match {
        case Some(found) => Future.successful(found.passwordInfo)
        case _ => Future.successful(None)
      }
    }
  }
 
  def save(profile: BasicProfile,mode: SaveMode): Future[models.SecureUser] = {
    DB.withSession{ implicit s =>
      val u = SecureUsers.save(profile,mode)
      Future.successful(u)
    }
  }
 
  def saveToken(token: MailToken): Future[MailToken] = {
    DB.withSession{ implicit s =>
      Future.successful(Tokens.save(token))
    }
  }
 
  def updatePasswordInfo(user: SecureUser,info: PasswordInfo): Future[Option[BasicProfile]] = {
    DB.withSession{ implicit s =>
      SecureUsers.updatePasswordInfo(user, info) match {
        case Some(u) => Future.successful(Some(ProfileFromUser(Some(u))))
        case _ => Future.successful(None)
      }
    }
  }

}

object ProfileFromUser {
  def apply(i: Option[SecureUser]): BasicProfile = {
    val user = i.get
    BasicProfile(user.userId, user.providerId, user.firstName, user.lastName, user.fullName,user.email,user.avatarUrl,user.authMethod,user.oAuth1Info,user.oAuth2Info,user.passwordInfo)
  } 
}
