package services

import play.api.Logger
import models._
import play.api.db.slick.DB
import play.api.Play.current

trait StandardsServiceTrait {
  def create(standard: Standard): Standard
  def create(standard: Standard, educationLevel: EducationLevel): Standard 
  def update(id:Long, standard: Standard): Int
  def find(id: Long): Option[Standard]
  def find(title: String): List[Standard]
  def list: List[Standard]
  def delete(standard: Standard) : Int 
  


}

class StandardsService extends StandardsServiceTrait {

  def create(standard: Standard) = {
    DB.withSession{ implicit s=>
      Standards.insert(standard)
    }
  }

  def create(standard: Standard, educationLevel: EducationLevel) = {
    DB.withSession{ implicit s=>
      

      Standards.insert(standard)
    }
  }

  def update(id: Long, standard: Standard) = {
    DB.withSession{ implicit s=>
      Standards.update(id, standard)
    }
  }
  
  def find(id: Long) = {
    DB.withSession{ implicit s=>
      Standards.find(id)
    }
  }

  def find(title: String) = {
    DB.withSession{ implicit s=>
      Standards.find(title)
    }
  }

  def list = {
    DB.withSession{ implicit s=>
      Standards.list
    }
  }

  def delete(standard: Standard) : Int = {
    DB.withSession{ implicit s=>
      Standards.delete(standard)
    }
  }

}
