package services

import models._
import play.api.Play.current
import play.api.db.slick.DB

trait DummyServiceTrait {
  def create(d: Dummy): Dummy
  def update(id: Long, d: Dummy): Int
  def list: List[Dummy]
  def delete(d: Dummy): Int
  def find(id: Long): Option[Dummy]
}

class DummyService extends DummyServiceTrait {

  def list = {
    DB.withSession { implicit s =>
      Dummies.list
    }
  }

  def create(d: Dummy) = {
    DB.withSession { implicit s =>
      Dummies.insert(d)
    }
  }

  def update(id: Long, d: Dummy) = {
    DB.withSession { implicit s =>
      Dummies.update(id, d)
    }
  }

  def delete(d: Dummy) = {
    DB.withSession { implicit s =>
      Dummies.delete(d)
    }
  }

  def find(id: Long) = {
    DB.withSession { implicit s =>
      Dummies.find(id)
    }
  }
}
