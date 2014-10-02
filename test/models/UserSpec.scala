package models


import play.api.db.slick.DB
import play.api.Play.current

import collection.mutable.Stack
import scala.concurrent.Future
import org.scalatest._
import org.scalatestplus.play._

import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._

import org.mindrot.jbcrypt._


class UserSpec extends PlaySpec with Results {
  
  import models._

  // -- Date helpers
  def dateIs(date: java.util.Date, str: String) = new java.text.SimpleDateFormat("yyyy-MM-dd").format(date) == str
  val p: String = BCrypt.hashpw("password", BCrypt.gensalt()) 

  "User model" should {
    "insert and retrieve new record by id" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession{ implicit s =>
          val u: User = new User(id = None, firstname ="Matt", lastname ="Silbernagel", email = "silbermm@gmail.com", passwordHash = "password", dateJoined = None)
          Users.insert(u);
          val Some(us) = Users.findById(1)
          us.firstname mustEqual "Matt"
        }
      }
    }

    "insert and retrieve a new record by email" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession{ implicit s =>
          val u: User = new User(id = None, firstname ="Matt", lastname ="Silbernagel", email = "silbermm@gmail.com", passwordHash = "password", dateJoined = None)
          Users.insert(u);
          val Some(us) = Users.findByEmail("silbermm@gmail.com")
          us.firstname mustEqual "Matt"
        }
      }
    }

    "not allow two of the same emails" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession{ implicit s =>
          val u: User = new User(id = None, firstname ="Matt", lastname ="Silbernagel", email = "silbermm@gmail.com", passwordHash = "password", dateJoined = None)
          Users.insert(u);
          val u2: User = new User(None, "Matt","Sil","silbermm@gmail.com","password",None) 
          a [org.h2.jdbc.JdbcSQLException] should be thrownBy {
            Users.insert(u2)  
          }
        }
      }
    }

    "match hashed passwords" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession{ implicit s =>
          val u: User = new User(None,"Matt","Silbernagel","silbermm@gmail.com",p,None)
          Users.insert(u);
          val Some(us) = Users.findByEmail("silbermm@gmail.com")
          BCrypt.checkpw("password",p) mustBe true
        }
      }
    }

  }
  

}
