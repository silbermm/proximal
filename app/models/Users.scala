package models

import java.util.Date
import java.sql.{ Date => SqlDate }
import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
import play.api.Play.current
import securesocial.core._

case class User(id: Option[Long], firstname: String, 
                 lastname: String, email: String, passwordHash: String, 
                 dateJoined: Option[Date])

case class SecureUser(uid: Option[Long] = None, providerId: String,userId: String,firstName: Option[String],lastName: Option[String],
                      fullName: Option[String], email: Option[String], avatarUrl: Option[String], authMethod: AuthenticationMethod,
                      oAuth1Info: Option[OAuth1Info] = None, oAuth2Info: Option[OAuth2Info] = None, passwordInfo: Option[PasswordInfo] = None) extends UserProfile

class SecureUsers(tag: Tag) extends Table[SecureUser](tag, "SecureUser"){
  
  implicit def string2AuthenticationMethod: TypeMapper[AuthenticationMethod] =
    MappedTypeMapper.base[AuthenticationMethod, String](
      authenticationMethod => authenticationMethod.method,
      string => AuthenticationMethod(string)
    )

  implicit def tuple2OAuth1Info(tuple: (Option[String], Option[String])) = 
    tuple match {
      case (Some(token), Some(secret)) => Some(OAuth1Info(token, secret))
      case _ => None
    }
   
  implicit def tuple2OAuth2Info(tuple: (Option[String], Option[String],
    Option[Int], Option[String])) = tuple match {
      case (Some(token), tokenType, expiresIn, refreshToken) => Some(OAuth2Info(token, tokenType, expiresIn, refreshToken))
      case _ => None
    }
  
  implicit def tuple2PasswordInfo(tuple: (String, String, Option[String])) =
    tuple match {
      case (Some(hasher), Some(password), salt) => Some(PasswordInfo(hasher, password, salt))
      case _ => None
    } 
  
  def uid = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def providerId = column[String]("providerId")
  def userId = column[String]("userId")
  def firstName = column[String]("firstName")
  def lastName = column[String]("lastName")
  def email = column[String]("email")
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
  def hasher = column[String]("hasher")
  def password = column[String]("password")
  def salt = column[String]("salt")

  def * = (uid.?,providerId,userId,firstName,lastName,email,avatarUrl,
          authMethod,token,secret,accessToken,tokentType,expiresIn,refreshToken,
          hasher,password,salt) <> ( 
            u => SecureUser( u._1,u._2,u._3,u._4,u._5,u._6,u._7,
                           (u._8,u._9),(u._10,u._11,u._12,u._13),(u._14,u._15)), 
            (u:SecureUser) => Some{
              (
                u.uid,u.providerId,u.userId,u.firstName,u.lastName,u.email,u.avatarUrl,u.oAuth1Info.map(_.token),
                u.oAuth1Info.map(_.secret), u.oAuth2Info.map(_.accessToken), u.oAuth2Info.flatMap(_.tokenType),
                u.oAuth2Info.flatMap(_.expiresIn), u.oAuth2Info.flatMap(_.refreshToken), u.passwordInfo.map(_.hasher),
                u.passwordInfo.map(_.password), u.passwordInfo.flatMap(_.salt)
              )
            }
          )
}




class Users (tag: Tag) extends Table[User](tag, "User") {

  implicit val dateColumnType = MappedColumnType.base[Date, Long](d => d.getTime, d => new Date(d))

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def firstname = column[String]("firstname", O.NotNull)
  def lastname = column[String]("lastname", O.NotNull)
  def email = column[String]("email", O.NotNull)
  def uniqueEmail = index("idx_email", email, unique=true)
  def passwordHash = column[String]("password", O.NotNull)
  def dateJoined = column[Date]("dateJoined", O.Nullable, O.Default(new Date()))

  def * = (id.?, firstname,lastname,email,passwordHash,dateJoined.?) <> (User.tupled, User.unapply _)
}

object Users {
  
  val users = TableQuery[Users]

  /**
   * Retrieve a user by id 
   */
  def findById(id: Long)(implicit s: Session) : Option [User] = 
     users.filter(_.id === id).firstOption
  /**
   * Retrieve a user by email address
   */
  def findByEmail(email: String)(implicit s: Session) : Option[User] =
    users.filter(_.email === email).firstOption
  /**
   * Count all users
   */
  def count(implicit s: Session) : Int = 
    Query(users.length).first
  /**
   * Count users with a filter 
   */
  def count(filter: String)(implicit s: Session) : Int =
    Query(users.filter(_.email.toLowerCase like filter.toLowerCase).length).first
  /**
   * Insert a new user
   * @param user
   */
  def insert(user: User)(implicit s: Session) {
    users.insert(user)
  }
  /**
    * Update a user
    * @param id
    * @param user
    */
  def update(id: Long, user: User)(implicit s: Session) {
    val userToUpdate: User = user.copy(Some(id))
    users.filter(_.id === id).update(userToUpdate)
  }
  /**
   * Delete a user
   * @param id
   */
  def delete(id: Long)(implicit s: Session) {
    users.filter(_.id === id).delete
  }
}
