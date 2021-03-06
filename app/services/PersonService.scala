package services

import models._
import play.api.Play.current
import play.api.db.slick.DB
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import play.Logger

import scala.concurrent.Future

class PersonService {

  def findPerson(id: Long): Option[Person] = {
    DB.withSession { implicit s =>
      People.findPerson(id)
    }
  }

  def findChildWithEducationLevel(id: Long): (Person, EducationLevel) = {
    DB.withSession { implicit s =>
      People.findWithEducationLevel(id)
    }
  }

  def findPersonByUid(uid: Long): Option[Person] = {
    DB.withSession { implicit s =>
      People.findPersonByUid(uid)
    }
  }

  def deletePerson(id: Long) = {
    DB.withSession { implicit s =>
      People.deletePerson(id)
    }
  }

  def createPerson(p: Person) = {
    DB.withSession { implicit s =>
      People.insertPerson(p)
    }
  }

  def createPerson(p: Person, e: EducationLevel) = {
    DB.withSession { implicit s =>
      EducationLevels.find(e.description).map(ed =>
        People.insertPerson(p.copy(educationLevelId = ed.id))
      ).getOrElse({
        val newEd = EducationLevels.insert(e)
        People.insertPerson(p.copy(educationLevelId = newEd.id))
      })
    }
  }

  def updatePerson(id: Long, p: Person): Option[Person] = {
    DB.withSession { implicit s =>
      People.updatePerson(id, p)
    }
  }

  def addAdminRole(p: Person): Int = {
    DB.withSession { implicit s =>
      People.addAdminRole(p)
    }
  }

  def addChild(child: Person, parent: Person): Relationship = {
    DB.withSession { implicit s =>
      People.addChild(child, parent)
    }
  }

  /**
   * Finds all children that belong to a parent
   * @param uid the parents login account number
   */
  def findChildren(uid: Long) = {
    DB.withSession { implicit s =>
      People.findPersonByUid(uid) match {
        case Some(p) => {
          Logger.debug("found a person with that uid!")
          People.findChildrenFor(p.id getOrElse 0)
        }
        case _ => List()
      }
    }
  }

  def createRole(role: Role): Role = {
    DB.withSession { implicit s =>
      Roles.insert(role)
    }
  }

  def findRoles(secureUserId: Long) = {
    DB.withSession { implicit s =>
      People.findPersonByUid(secureUserId) match {
        case Some(person) => People.findRoles(person)
        case _ => List.empty
      }
    }
  }

  def listRoles: List[Role] = {
    DB.withSession { implicit s =>
      Roles.list
    }
  }

  def isAdmin(id: Long): Boolean = {
    DB.withSession { implicit s =>
      People.findPersonByUid(id).map(person => {
        People.findRoles(person).find(_.name == "admin") match {
          case Some(role) => true
          case _ => false
        }
      }).getOrElse(
        false
      )
    }
  }

  def updateRole(id: Long, r: Role): Int = {
    DB.withSession { implicit s =>
      Roles.update(id, r)
    }
  }

  def deleteRole(id: Long): Int = {
    DB.withSession { implicit s =>
      Roles.delete(id)
    }
  }

}

object PersonService {

  import play.api.mvc._

  val personService = new PersonService

  def childAction(uid: Long, childId: Long, f: Long => Result): Result = {
    // get the children for this person
    personService.findChildren(uid) match {
      case children: List[Person] => {
        children.find(c => c.id.get == childId) match {
          case Some(ch) => f(ch.id.get)
          case None => play.api.mvc.Results.Unauthorized
          case _ => play.api.mvc.Results.Ok
        }
      }
      case _ => play.api.mvc.Results.NoContent
    }
  }

  def isChildOf[T](uid: Long, childId: Long, f: Long => Option[T]): Option[T] = {
    // get the children for this person
    personService.findChildren(uid) match {
      case children: List[Person] => {
        children.find(c => c.id.get == childId) match {
          case Some(ch) => f(ch.id.get)
          case None => None
          case _ => None
        }
      }
      case _ => None
    }
  }

  def childActionAsync(uid: Long, childId: Long, f: Long => Future[Result]): Future[Result] = {
    personService.findChildren(uid) match {
      case children: List[Person] => {
        children.find(c => c.id.get == childId) match {
          case Some(ch) => f(ch.id.get)
          case None => scala.concurrent.Future { play.api.mvc.Results.Unauthorized }
          case _ => scala.concurrent.Future { play.api.mvc.Results.Ok }
        }
      }
      case _ => scala.concurrent.Future { play.api.mvc.Results.NoContent }
    }
  }

  def findPersonByUid(uid: Long) = {
    personService.findPersonByUid(uid);
  }

}
