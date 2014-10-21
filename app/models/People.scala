package models

import java.util.Date
import java.sql.{ Date => SqlDate }
import org.joda.time.DateTime
import com.github.tototoshi.slick.PostgresJodaSupport._
import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
import scala.slick.SlickException
import scala.slick.lifted.ProvenShape
import play.api.Play.current
import play.api.Logger

case class Person(id: Option[Long] = None, firstName:String, lastName: Option[String], birthDate: Option[DateTime], uid: Option[Long])
class People(tag: Tag) extends Table[Person](tag, "Person"){ 

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def firstName = column[String]("firstName")
  def lastName = column[Option[String]]("lastName")
  def birthDate = column[Option[DateTime]]("birthDate")
  def uid = column[Option[Long]]("uid")

  def * = (id.?, firstName,lastName, birthDate, uid) <> (Person.tupled, Person.unapply _)  

  def idx = index("idx_uid", (uid), unique = true)

}

case class RelationshipType(id: Option[Long] = None, reltype: String, description: String)
class RelationshipTypes(tag: Tag) extends Table[RelationshipType](tag,"RelationshipType") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def reltype = column[String]("type")
  def description = column[String]("description")
  
  def * = (id.?, reltype, description) <> (RelationshipType.tupled, RelationshipType.unapply _)
}

case class Relationship(personId: Long, otherPersonId: Long, typeId: Long)
class Relationships(tag: Tag) extends Table[Relationship](tag, "Relationship") {
  
  val people = TableQuery[People]
  val relationship_types = TableQuery[RelationshipTypes] 

  def personId = column[Long]("personId")
  def otherPersonId = column[Long]("otherId")
  def typeId = column[Long]("typeId")

  def * = (personId, otherPersonId, typeId) <> (Relationship.tupled, Relationship.unapply _)
/*
  def person = foreignKey("PERSON_FK", personId, people)(_.id)
  def otherPerson = foreignKey("OTHER_PERSON_FK", otherPersonId, people)(_.id)
  def relationshipType = foreignKey("RELTYPE_FK", typeId, relationship_types)(_.id) 
*/
}



object People {
  val people = TableQuery[People]
  val relationship_types = TableQuery[RelationshipTypes]
  val relationships = TableQuery[Relationships]
 
  def insertPerson(p : Person)(implicit s: Session) : Person = {
    p.uid match {
      case Some(u) => {
        findPersonByUid(u) match {
          case Some(person) => {
            updatePerson(person.id.get, person) match {
              case Some(p) => p
              case _ => throw new Exception("unable to insert/update the user")
            }
          }
          case None => (people returning people.map(_.id) into ((people,id) => people.copy(id=Some(id)))) += p
        }
      }
      case None =>  (people returning people.map(_.id) into ((people,id) => people.copy(id=Some(id)))) += p
    }
  }

  def updatePerson(id: Long, p: Person)(implicit s: Session) = {
    val personToUpdate: Person = p.copy(Some(id))
    people.filter(_.id === id).update(personToUpdate)
    findPerson(id) 
  }

  def findPersonByUid(uid: Long)(implicit s: Session) =
    people.filter(_.uid === uid).firstOption

  def findPerson(id: Long)(implicit s: Session) =
    people.filter(_.id === id).firstOption

  def deletePerson(id: Long)(implicit s: Session) = {
    people.filter(_.id === id).delete 
  }

  def addChild(child: Person, parent: Person)(implicit s: Session) = {
    // add a child to another person
    // get the id of the relationship type that is a child... 
    // if no type for child is found, create one
    val childTypeId : Long = findRelTypeByName("child") match {
      case Some(c) => c.id.get
      case None => {
        // create a child type and return the generated id
        relationship_types returning relationship_types.map(_.id) += new RelationshipType(None, "child", "A child of a parent that is registered in the system") 
      }
    }
    // we now have an id of the relationship type... use it to insert the two people into the relationship table
    val r = new Relationship(parent.id.get, child.id.get, childTypeId) 
    relationships += r
    r
  }

  def findChildrenFor(uid: Long)(implicit s: Session) = {
    val q3 = (for {
      ((p, r), t) <- people leftJoin relationships on (_.id === _.personId) leftJoin relationship_types on (_._2.typeId === _.id)
      if t.reltype === "child" 
      //t <- relationship_types if r.typeId === t.id     
    } yield (r.otherPersonId))
    val listOfChildIds = q3.list
  
  }

  def findRelTypeById(id: Long)(implicit s: Session): Option[RelationshipType] = 
    relationship_types.filter(_.id === id).firstOption

  def findRelTypeByName(name: String)(implicit s: Session): Option[RelationshipType] = 
    relationship_types.filter(_.reltype === name).firstOption

}
