package models

import play.api.db.slick.Config.driver.simple._

class Cocktails(tag: Tag) extends Table[(Long, String)](tag, "COCKTAIL") {
  def id = column[Long]("ID")
  def name = column[String]("NAME")
  def * = (id,name)
}
