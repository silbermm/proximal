package services

import play.api.Logger
import models._
import play.api.db.slick.DB
import play.api.Play.current
import org.joda.time.DateTime

class PersonService {

  def findPerson(id: Long) : Option[Person] = {
    DB.withSession{ implicit s=>
      People.findPerson(id)
    }
  }

  def findChildWithEducationLevel(id: Long) : (Person,EducationLevel) = {
    DB.withSession{ implicit s=>
      People.findWithEducationLevel(id)
    }
  }

  def findPersonByUid(uid: Long) : Option[Person] = {
    DB.withSession{implicit s =>
      People.findPersonByUid(uid)
    }
  }

  def deletePerson(id: Long) = {
    DB.withSession{implicit s =>
      People.deletePerson(id)
    }
  }

  def createPerson(p: Person) = {
    DB.withSession{implicit s =>
      People.insertPerson(p)
    }
  }

  def createPerson(p: Person, e: EducationLevel) = {
    DB.withSession{implicit s=>
      EducationLevels.find(e.description).map( ed=>
        People.insertPerson(p.copy(educationLevelId = ed.id))
      ).getOrElse({
        val newEd = EducationLevels.insert(e)
        People.insertPerson(p.copy(educationLevelId = newEd.id))
      })
    }
  }

  def updatePerson(id: Long, p: Person) : Option[Person] = {
    DB.withSession{implicit s =>
      People.updatePerson(id,p)
    }
  }

  def addChild(child: Person, parent: Person) : Relationship = {
    DB.withSession{implicit s =>
      People.addChild(child,parent)
    }
  }
 
  /**
   * Finds all children that belong to a parent
   * @param uid the parents login account number
   */
  def findChildren(uid: Long) = {
    DB.withSession{implicit s =>
      People.findChildrenFor(uid)
    }
  }
}
