package models

import java.util.Date
import java.sql.{ Date => SqlDate }
import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
import play.api.Play.current

case class User(id: Option[Long], firstname: String, 
                 lastname: String, email: String, passwordHash: String, 
                 dateJoined: Option[Date])


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
