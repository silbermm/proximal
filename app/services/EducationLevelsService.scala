package services

import models._
import play.api.Play.current
import play.api.db.slick.DB

trait EducationLevelsServiceTrait {
  def create(e: EducationLevel): EducationLevel
  def create(e: Seq[EducationLevel]): Option[Int]
  def update(id: Long, e: EducationLevel): Int
  def find(id: Long): Option[EducationLevel]
  def find(description: String): Option[EducationLevel]
  def findWithStandards(id: Long): List[(EducationLevel, Standard)]
  def findByChild(child: Person): Option[EducationLevel]
  def list: List[EducationLevel]
  def delete(e: EducationLevel): Int

  def createStandardLevel(standardLevel: StandardLevel): StandardLevel
}

class EducationLevelsService extends EducationLevelsServiceTrait {

  def create(e: EducationLevel) = {
    DB.withSession { implicit s =>
      EducationLevels.insert(e)
    }
  }

  def create(e: Seq[EducationLevel]) = {
    DB.withSession { implicit s =>
      EducationLevels.insert(e)
    }
  }

  def createStandardLevel(st: StandardLevel): StandardLevel = {
    DB.withSession { implicit s =>
      StandardLevels.insert(st)
    }
  }

  def update(id: Long, e: EducationLevel) = {
    DB.withSession { implicit s =>
      EducationLevels.update(id, e)
    }
  }

  def find(id: Long) = {
    DB.withSession { implicit s =>
      EducationLevels.find(id)
    }
  }

  def find(description: String) = {
    DB.withSession { implicit s =>
      EducationLevels.find(description)
    }
  }

  def findWithStandards(id: Long) = {
    DB.withSession { implicit s =>
      EducationLevels.findWithStandards(id)
    }
  }

  def findByChild(child: Person) = {
    DB.withSession { implicit s =>
      EducationLevels.findByChild(child)
    }
  }

  def list = {
    DB.withSession { implicit s =>
      EducationLevels.list
    }
  }

  def delete(e: EducationLevel) = {
    DB.withSession { implicit s =>
      EducationLevels.delete(e)
    }
  }
}

