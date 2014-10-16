package models

import java.util.Date
import java.sql.{ Date => SqlDate }
import org.joda.time.DateTime
import com.github.tototoshi.slick.PostgresJodaSupport._
import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
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

  def person = foreignKey("PERSON_FK", personId, people)(_.id)
  def otherPerson = foreignKey("OTHER_PERSON_FK", otherPersonId, people)(_.id)
  def relationshipType = foreignKey("RELTYPE_FK", typeId, relationship_types)(_.id) 
}



object People {
  val people = TableQuery[People]
  val relationship_types = TableQuery[RelationshipTypes]
  val relationships = TableQuery[Relationships]
 
  def insertPerson(p : Person)(implicit s: Session) = 
    people += p

  def findPerson(id: Long)(implicit s: Session) =
    people.filter(_.id === id).firstOption

}
