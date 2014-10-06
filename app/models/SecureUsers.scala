package models

import java.util.Date
import java.sql.{ Date => SqlDate }
import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
import scala.slick.lifted.ProvenShape
import play.api.Play.current
import securesocial.core._
import securesocial.core.services._


case class SecureUser(uid: Option[Long] = None, providerId: String,userId: String,firstName: Option[String],lastName: Option[String],
                      fullName: Option[String], email: Option[String], avatarUrl: Option[String], authMethod: AuthenticationMethod,
                      oAuth1Info: Option[OAuth1Info] = None, oAuth2Info: Option[OAuth2Info] = None, passwordInfo: Option[PasswordInfo] = None) extends UserProfile

class SecureUsers(tag: Tag) extends Table[SecureUser](tag, "SecureUser"){
 
 implicit def string2AuthenticationMethod = MappedColumnType.base[AuthenticationMethod, String](
     authenticationMethod => authenticationMethod.method, string => AuthenticationMethod(string))
 
  implicit def tuple2OAuth1Info(tuple: (Option[String], Option[String])): Option[OAuth1Info] = 
    tuple match {
      case (Some(token), Some(secret)) => Some(OAuth1Info(token, secret))
      case _ => None
    }
   
  implicit def tuple2OAuth2Info(tuple: (Option[String], Option[String], Option[Int], Option[String])): Option[OAuth2Info] = tuple match {
      case (Some(token),tokenType,expiresIn,refreshToken) => Some(OAuth2Info(token, tokenType, expiresIn, refreshToken))
      case _ => None
    }
  
  implicit def tuple2PasswordInfo(tuple: (Option[String], Option[String], Option[String])):Option[PasswordInfo] =
    tuple match {
      case (Some(hasher), Some(password), salt) => Some(PasswordInfo(hasher, password, salt))
      case _ => None
    } 
  
  def uid = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def providerId = column[String]("providerId")
  def userId = column[String]("userId")
  def firstName = column[Option[String]]("firstName")
  def lastName = column[Option[String]]("lastName")
  def fullName = column[Option[String]]("fullName")
  def email = column[Option[String]]("email")
  def avatarUrl = column[Option[String]]("avatarUrl")
  def authMethod = column[AuthenticationMethod]("authMethod")
  
  // oAuth 1
  def token = column[Option[String]]("token")
  def secret = column[Option[String]]("secret")
  
  // oAuth 2
  def accessToken = column[Option[String]]("accessToken")
  def tokenType = column[Option[String]]("tokenType")
  def expiresIn = column[Option[Int]]("expiresIn")
  def refreshToken = column[Option[String]]("refreshToken")

  // passwordInfo 
  def hasher = column[Option[String]]("hasher")
  def password = column[Option[String]]("password")
  def salt = column[Option[String]]("salt")

  def * : ProvenShape[SecureUser] = {
    val shapedValue = (uid.?,
         userId,
         providerId,
         firstName,
         lastName,
         fullName,
         email,
         avatarUrl,
         authMethod,
         token,
         secret,
         accessToken,
         tokenType,
         expiresIn,
         refreshToken,
         hasher,
         password,
         salt).shaped
    
    shapedValue.<>({
      tuple => 
        SecureUser.apply( uid = tuple._1,
                          providerId = tuple._2,
                          userId = tuple._3,
                          firstName = tuple._4,
                          lastName = tuple._5,
                          fullName = tuple._6,
                          email = tuple._7,
                          avatarUrl = tuple._8,
                          authMethod = tuple._9,
                          oAuth1Info = (tuple._10, tuple._11),
                          oAuth2Info = (tuple._12,tuple._13,tuple._14,tuple._15),
                          passwordInfo = (tuple._16,tuple._17,tuple._18))
    },{
      (u:SecureUser) => Some{
        (
          u.uid,
          u.providerId,
          u.userId,
          u.firstName,
          u.lastName,
          u.fullName,
          u.email,
          u.avatarUrl,
          u.authMethod,
          u.oAuth1Info.map(_.token),
          u.oAuth1Info.map(_.secret), 
          u.oAuth2Info.map(_.accessToken),
          u.oAuth2Info.flatMap(_.tokenType),
          u.oAuth2Info.flatMap(_.expiresIn), 
          u.oAuth2Info.flatMap(_.refreshToken), 
          u.passwordInfo.map(_.hasher),
          u.passwordInfo.map(_.password), 
          u.passwordInfo.flatMap(_.salt)
        )
      }
    })
  
  }

}

object UserFromProfile {
  def apply(i: BasicProfile): SecureUser = SecureUser(None, i.userId, i.providerId, i.firstName, i.lastName, i.fullName,
    i.email, i.avatarUrl, i.authMethod, i.oAuth1Info, i.oAuth2Info, i.passwordInfo)
}

object SecureUsers {
  val secureUsers = TableQuery[SecureUsers]

  def findById(uid: Long)(implicit s: Session): Option[SecureUser] =
    secureUsers.filter(_.uid === uid).firstOption

  def findByEmailAndProvider(email: String, providerId: String)(implicit s:Session): Option[SecureUser] =
    secureUsers.filter(x => x.email === email && x.providerId === providerId).firstOption

  def findByProviderIdAndUserId(providerId: String,userId: String)(implicit s:Session): Option[SecureUser] =
    secureUsers.filter(x => x.userId === userId && x.providerId === providerId).firstOption

  def save(user: BasicProfile, mode: SaveMode)(implicit s: Session): SecureUser = { 
    findByProviderIdAndUserId(user.providerId,user.userId) match {
      case None => {
        val u = UserFromProfile(user) 
        secureUsers.insert(u) 
        u 
      }
      case Some(existingUser) => {
        existingUser 
      }
    }
  }

  def updatePasswordInfo(user: SecureUser, passwordInfo: PasswordInfo)(implicit s: Session): Option[SecureUser] = {
    val userToUpdate: SecureUser = user.copy(passwordInfo = Some(passwordInfo))
    val i = secureUsers.filter(_.uid === user.uid).update(userToUpdate)
    if(i > 0){
      findByProviderIdAndUserId(user.providerId,user.userId)
    } else {
      None
    }
  }
  

}



