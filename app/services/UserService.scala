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

  def link(current: models.SecureUser,to: securesocial.core.BasicProfile): scala.concurrent.Future[models.SecureUser] = ???
  
  def passwordInfoFor(user: models.SecureUser): Future[Option[PasswordInfo]] = {
    DB.withSession{ implicit s => 
      
    }
  }
 
  def save(profile: securesocial.core.BasicProfile,mode: securesocial.core.services.SaveMode): scala.concurrent.Future[models.SecureUser] = ???
 
  def saveToken(token: securesocial.core.providers.MailToken): scala.concurrent.Future[securesocial.core.providers.MailToken] = ???
 
  def updatePasswordInfo(user: models.SecureUser,info: securesocial.core.PasswordInfo): scala.concurrent.Future[Option[securesocial.core.BasicProfile]] = ???


}

object ProfileFromUser {
  def apply(i: Option[SecureUser]): BasicProfile = {
    val user = i.get
    BasicProfile(user.userId, user.providerId, user.firstName, user.lastName, user.fullName,user.email,user.avatarUrl,user.authMethod,user.oAuth1Info,user.oAuth2Info,user.passwordInfo)
  } 
}
