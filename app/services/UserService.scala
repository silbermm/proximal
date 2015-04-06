/* Copyright 2015 Matt Silbernagel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package services

import models._
import org.joda.time.DateTime
import play.api.Play.current
import play.api.db.slick.DB
import securesocial.core._
import securesocial.core.providers.MailToken
import securesocial.core.services.{ SaveMode, UserService }

import scala.concurrent.Future

class SecureUserService extends UserService[SecureUser] {

  def deleteExpiredTokens(): Unit = {
    DB.withSession { implicit s =>
      Tokens.deleteExpiredTokens(new DateTime)
    }
  }

  def deleteToken(uuid: String): Future[Option[MailToken]] = {
    DB.withSession { implicit s =>
      val t = Tokens.delete(uuid)
      Future.successful(t)
    }
  }

  def find(providerId: String, userId: String): Future[Option[BasicProfile]] = {
    DB.withSession { implicit s =>
      val u: Option[SecureUser] = SecureUsers.findByProviderIdAndUserId(providerId, userId)
      u match {
        case Some(user) => {
          Future.successful(Some(ProfileFromUser(user)))
        }
        case _ => {
          Future.successful(None)
        }
      }
    }
  }

  def findByEmailAndProvider(email: String, providerId: String): Future[Option[BasicProfile]] = {
    DB.withSession { implicit s =>
      val u: Option[SecureUser] = SecureUsers.findByEmailAndProvider(email, providerId)
      u match {
        case Some(user) => Future.successful(Some(ProfileFromUser(user)))
        case _ => Future.successful(None)
      }
    }
  }

  def findToken(token: String): Future[Option[MailToken]] = {
    DB.withSession { implicit s =>
      Future.successful(Tokens.findById(token))
    }
  }

  def link(current: SecureUser, to: BasicProfile): Future[models.SecureUser] = {
    // get the current user from the database...
    if (current.providerId == to.providerId && current.userId == to.userId) {
      Future.successful(current)
    } else {
      Future.successful(current)
    }
  }

  def passwordInfoFor(user: models.SecureUser): Future[Option[PasswordInfo]] = {
    DB.withSession { implicit s =>
      SecureUsers.findById(user.uid.get) match {
        case Some(found) => Future.successful(found.passwordInfo)
        case _ => Future.successful(None)
      }
    }
  }

  def save(profile: BasicProfile, mode: SaveMode): Future[models.SecureUser] = {
    DB.withSession { implicit s =>
      val u = SecureUsers.save(profile, mode)
      val uid = u.uid match {
        case Some(userId) => Some(userId)
        case None => None
      }
      val person = new Person(None, u.firstName.get, u.lastName, None, None, uid)
      People.insertPerson(person)
      Future.successful(u)
    }
  }

  def saveToken(token: MailToken): Future[MailToken] = {
    DB.withSession { implicit s =>
      Future.successful(Tokens.save(token))
    }
  }

  def updatePasswordInfo(user: SecureUser, info: PasswordInfo): Future[Option[BasicProfile]] = {
    DB.withSession { implicit s =>
      SecureUsers.updatePasswordInfo(user, info) match {
        case Some(u) => Future.successful(Some(ProfileFromUser(u)))
        case _ => Future.successful(None)
      }
    }
  }
}

object ProfileFromUser {
  def apply(user: SecureUser): BasicProfile = {
    BasicProfile(user.providerId,
      user.userId,
      user.firstName,
      user.lastName,
      user.fullName,
      user.email,
      user.avatarUrl,
      user.authMethod,
      user.oAuth1Info,
      user.oAuth2Info,
      user.passwordInfo
    )
  }
}
