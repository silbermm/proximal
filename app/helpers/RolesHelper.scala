package helpers

import models._
import play.api.Play.current
import play.api.db.slick.DB
import play.api.mvc._

object RolesHelper {

  def admin(uid: Long, f: Long => Result): Result = {
    DB.withSession { implicit s =>
      if (People.isAdminByUid(uid)) {
        f(uid)
      } else {
        play.api.mvc.Results.Unauthorized;
      }
    }
  }

  def isAdmin[T](uid: Long, f: Long => Option[T]): Option[T] = {
    DB.withSession { implicit s =>
      if (People.isAdminByUid(uid)) {
        f(uid)
      } else {
        None
      }
    }
  }

}
