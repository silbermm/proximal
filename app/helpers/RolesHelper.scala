package helpers

import play.api._
import play.api.mvc._
import models._
import securesocial.core._

import play.api.db.slick.DB
import play.api.Play.current

object RolesHelper {
  
  def admin( uid: Long, f: Long => Result ) : Result = { 
    DB.withSession{ implicit s =>
      if( People.isAdminByUid(uid )) {
        f(uid)
      } else {
        play.api.mvc.Results.Unauthorized;
      }
    }
  }

}
