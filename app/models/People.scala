package models

import java.util.Date
import org.joda.time.DateTime
import com.github.tototoshi.slick.PostgresJodaSupport._
import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
import scala.slick.SlickException
import scala.slick.lifted.ProvenShape
import play.api.Play.current
import play.api.Logger

case class Person(id: Option[Long] = None, firstName: String, lastName: Option[String], birthDate: Option[DateTime], educationLevelId: Option[Long], uid: Option[Long])
class People(tag: Tag) extends Table[Person](tag, "Person") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def firstName = column[String]("firstName")
  def lastName = column[Option[String]]("lastName")
  def birthDate = column[Option[DateTime]]("birthDate")
  def educationLevelId = column[Option[Long]]("educationLevelId")
  def uid = column[Option[Long]]("uid")

  def * = (id.?, firstName, lastName, birthDate, educationLevelId, uid) <> (Person.tupled, Person.unapply _)

  def idx = index("idx_uid", (uid), unique = true)
  def educationLevel = foreignKey("educationLevel", educationLevelId, EducationLevels.education_levels)(_.id)

}

case class RelationshipType(id: Option[Long] = None, reltype: String, description: String)
class RelationshipTypes(tag: Tag) extends Table[RelationshipType](tag, "RelationshipType") {

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
}

case class Role(id: Option[Long], name: String, description: String)
class Roles(tag: Tag) extends Table[Role](tag, "roles") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def description = column[String]("description")

  def * = (id.?, name, description) <> (Role.tupled, Role.unapply _)
}

object Roles {

  lazy val roles = TableQuery[Roles]
  lazy val person_roles = PersonRoles.person_roles

  def insert(r: Role)(implicit s: Session): Role = {
    (roles returning roles.map(_.id) into ((roles, id) => roles.copy(id = Some(id)))) += r
  }

  def update(id: Long, r: Role)(implicit s: Session): Int = {
    val roleToUpdate = r.copy(Some(id))
    roles.filter(_.id === id).update(roleToUpdate)
  }

  def delete(id: Long)(implicit s: Session): Int = {
    roles.filter(_.id === id).delete
  }

  def list(implicit s: Session): List[Role] = {
    roles.list
  }

  def find(name: String)(implicit s: Session) = {
    roles.filter(_.name === name).firstOption
  }

  def find(id: Long)(implicit s: Session) = {
    roles.filter(_.id === id).firstOption
  }

}

case class PersonRole(id: Option[Long], personId: Long, roleId: Long)
class PersonRoles(tag: Tag) extends Table[PersonRole](tag, "person_roles") {

  lazy val people = People.people
  lazy val roles = Roles.roles

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def personId = column[Long]("person_id")
  def roleId = column[Long]("role_id")

  def * = (id.?, personId, roleId) <> (PersonRole.tupled, PersonRole.unapply _)

  def person = foreignKey("person_id", personId, people)(_.id)
  def role = foreignKey("role_id", roleId, roles)(_.id)

}

object PersonRoles {
  lazy val person_roles = TableQuery[PersonRoles]
}

object People {
  lazy val people = TableQuery[People]
  lazy val relationship_types = TableQuery[RelationshipTypes]
  lazy val relationships = TableQuery[Relationships]
  lazy val education_levels = EducationLevels.education_levels
  lazy val schools = Schools.schools
  lazy val attendences = Attendences.attendences
  lazy val roles = Roles.roles
  lazy val person_roles = PersonRoles.person_roles

  def insertPerson(p: Person)(implicit s: Session): Person = {
    p.uid match {
      case Some(u) => {
        findPersonByUid(u) match {
          case Some(person) => {
            updatePerson(person.id.get, person) match {
              case Some(p) => p
              case _ => throw new Exception("unable to insert/update the user")
            }
          }
          case None => (people returning people.map(_.id) into ((people, id) => people.copy(id = Some(id)))) += p
        }
      }
      case None => (people returning people.map(_.id) into ((people, id) => people.copy(id = Some(id)))) += p
    }
  }

  def updatePerson(id: Long, p: Person)(implicit s: Session) = {
    val personToUpdate: Person = p.copy(Some(id))
    people.filter(_.id === id).update(personToUpdate)
    findPerson(id)
  }

  def findPersonByUid(uid: Long)(implicit s: Session) =
    people.filter(_.uid === uid).firstOption

  def isAdminByUid(uid: Long)(implicit s: Session): Boolean = {
    people.filter(_.uid === uid).firstOption.map(person => {
      People.findRoles(person).find(_.name == "admin") match {
        case Some(role) => true
        case _ => false
      }
    }).getOrElse(
      false
    )
  }

  def findWithEducationLevel(id: Long)(implicit s: Session): (Person, EducationLevel) = {
    val query = for {
      child <- people if child.id === id
      level <- education_levels if level.id === child.educationLevelId
    } yield (child, level)
    query.first
  }

  def findPerson(id: Long)(implicit s: Session) =
    people.filter(_.id === id).firstOption

  def deletePerson(id: Long)(implicit s: Session) = {
    people.filter(_.id === id).delete
  }

  def addChild(child: Person, parent: Person)(implicit s: Session) = {
    val childTypeId: Long = findRelTypeByName("child") match {
      case Some(c) => c.id.get
      case None => {
        relationship_types returning relationship_types.map(_.id) += new RelationshipType(None, "child", "A child of a parent that is registered in the system")
      }
    }
    val r = new Relationship(parent.id.get, child.id.get, childTypeId)
    relationships += r
    r
  }

  def addRole(person: Person, role: Role)(implicit s: Session) = {
    person_roles += new PersonRole(None, person.id.get, role.id.get)
  }

  def removeRole(person: Person, role: Role)(implicit s: Session) = {
    person_roles.filter(_.personId === person.id).filter(_.roleId === role.id).delete
  }

  def findRoles(person: Person)(implicit s: Session): List[Role] = {
    val query = for {
      pr <- person_roles if pr.personId === person.id.get
      r <- pr.role
    } yield r
    query.list
  }

  def addAdminRole(person: Person)(implicit s: Session) = {
    val admin: Role = Roles.find("admin").getOrElse(Roles.insert(new Role(None, "admin", "Administrator of the entire system")))
    person_roles += new PersonRole(None, person.id.get, admin.id.get)
  }

  def findChildrenFor(uid: Long)(implicit s: Session): List[Person] = {
    val q3 = (for {
      ((p, r), t) <- people leftJoin relationships on (_.id === _.personId) leftJoin relationship_types on (_._2.typeId === _.id)
      if t.reltype === "child"
    } yield (r.otherPersonId))
    val listOfChildIds = q3.list
    val listOfChildren: List[Person] = listOfChildIds.flatMap(id => findPerson(id))
    listOfChildren
  }

  def findRelTypeById(id: Long)(implicit s: Session): Option[RelationshipType] =
    relationship_types.filter(_.id === id).firstOption

  def findRelTypeByName(name: String)(implicit s: Session): Option[RelationshipType] =
    relationship_types.filter(_.reltype === name).firstOption

  def findSchoolsForChild(id: Long)(implicit s: Session): List[School] = {
    val query = (for {
      ((p, a), s) <- people leftJoin attendences on (_.id === _.personId) leftJoin schools on (_._2.schoolId === _.id)
    } yield s)
    query.list
  }

  def addSchoolToChild(child: Person, school: School, startDate: Option[Date], endDate: Option[Date], grade: Option[Long])(implicit s: Session) = {

    def noId: School = {
      Schools.findByNameCityState(school.name, school.city, school.state.get) match {
        case Some(sch) => sch
        case None => Schools.insert(school)
      }
    }

    val schoolToUse = school.id match {
      case None => noId
      case Some(id) => school
    }

    val a: Attendence = new Attendence(child.id.get, school.id.get, startDate, endDate, grade)
    attendences.insert(a)
  }

}
