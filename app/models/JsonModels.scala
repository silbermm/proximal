package models

case class Child(id: Option[Long], firstName: String, lastName: String, birthDate: Long, educationLevel: Option[EducationLevel])

case class Profile(user: SecureUser, person: Option[Person], roles: List[Role])

