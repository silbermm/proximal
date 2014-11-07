package services

import play.api.Logger
import models._
import play.api.db.slick.DB
import play.api.Play.current

trait StandardsServiceTrait {
  def create(standard: Standard): Standard
  def create(standard: Standard, educationLevel: EducationLevel): Standard 
  def create(statement: Statement) : Statement 
  def create(statements: Seq[Statement]) : Option[Int]
  def update(id:Long, standard: Standard): Int
  def find(id: Long): Option[Standard]
  def find(title: String): List[Standard]
  def findWithEducationLevels(id: Long): (Option[Standard],List[EducationLevel])  
  def list: List[Standard]
  def delete(standard: Standard) : Int   
}

class StandardsService extends StandardsServiceTrait {

  def create(standard: Standard) = {
    DB.withSession{ implicit s=>
      Standards.insert(standard)
    }
  }

  def create(standard: Standard, edLevels: List[EducationLevel]) : Option[Standard] = { 
    DB.withSession{ implicit s=>
      val educationLevels = for { l <- edLevels } yield EducationLevels.insert(l)
      Standards.insert(standard) match {
        case stan: Standard => {
          for{ level <- educationLevels } StandardLevels.insert(new StandardLevel(None,level.id.get, stan.id.get))
          Some(stan)
        }
        case _ => None 
      } 
    } 
  }

  def create(standard: Standard, educationLevel: EducationLevel) = {
    DB.withSession{ implicit s=>
      Standards.insert(standard)
    }
  }

  def create(statement: Statement) = {
    DB.withSession{ implicit s=>
      Statements.insert(statement)
    }
  }

  def create(statements: Seq[Statement]) = {
    DB.withSession{ implicit s=>
      Statements.insert(statements)
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

  def findWithEducationLevels(id: Long) = {
    DB.withSession{ implicit s=>
      Standards.findWithEducationLevels(id);
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
