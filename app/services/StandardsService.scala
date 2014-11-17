package services

import play.api.Logger
import models._
import play.api.db.slick.DB
import play.api.Play.current

trait StandardsServiceTrait {
  def create(standard: Standard): Standard
  def create(standard: Standard, educationLevel: EducationLevel): Option[Standard]
  def create(standard: Standard, edLevels: List[EducationLevel]) : Option[Standard] 
  def create(statement: Statement) : Statement
  def create(statements: Seq[Statement]) : Option[Int]
  def create(statement: Statement, edLevels: List[EducationLevel]): (Option[Statement], List[EducationLevel])
  def update(id:Long, standard: Standard): Int
  def update(id: Long, statement: Statement): Int
  def find(id: Long): Option[Standard]
  def find(title: String): List[Standard]
  def findStatement(id: Long) : (Option[Statement], List[EducationLevel] ) 
  def findWithEducationLevels(id: Long): (Option[Standard],List[EducationLevel])  
  def findWithStatements(id: Long): (Option[Standard], List[Statement])
  def findWithStatementsAndLevels(id: Long): (Option[Standard], List[(Option[Statement], List[EducationLevel])]) 
  def list: List[Standard]
  def delete(standard: Standard) : Int   
}

class StandardsService extends StandardsServiceTrait {

  override def create(standard: Standard) = {
    DB.withSession{ implicit s=>
      Standards.insert(standard)
    }
  }

  override def create(standard: Standard, edLevels: List[EducationLevel]) = { 
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

  override def create(standard: Standard, educationLevel: EducationLevel) = {
    DB.withSession{ implicit s=>
      create(standard, List(educationLevel))     
    }
  }

  override def create(statement: Statement) = {
    DB.withSession{ implicit s=>
      Statements.insert(statement)
    }
  }

  override def create(statements: Seq[Statement]) = {
    DB.withSession{ implicit s=>
      Statements.insert(statements)
    }
  } 
  
  override def create(statement: Statement, edLevels: List[EducationLevel]) ={
    DB.withSession{ implicit s=> 
      val educationLevels = for { l <- edLevels } yield EducationLevels.insert(l)
      Statements.insert(statement) match {
        case state: Statement => {
          for{ level <- educationLevels } StatementLevels.insert(new StatementLevel(None,level.id.get, state.id.get))
          (Some(state),educationLevels)
        }
        case _ => (None,List.empty)
      } 
    }
  }

  override def update(id: Long, standard: Standard) = {
    DB.withSession{ implicit s=>
      Standards.update(id, standard)
    }
  }

  override def update(id: Long, statement: Statement) = {
    DB.withSession{ implicit s=>
      Statements.update(id, statement)
    }
  }
  
  override def find(id: Long) = {
    DB.withSession{ implicit s=>
      Standards.find(id)
    }
  }

  override def findWithEducationLevels(id: Long) = {
    DB.withSession{ implicit s=>
      Standards.findWithEducationLevels(id);
    }
  }
  
  override def find(title: String) = {
    DB.withSession{ implicit s=>
      Standards.find(title)
    }
  }

  def findWithStatements(id: Long) = {
    DB.withSession{ implicit s=>
      Standards.findWithStatements(id)
    }
  }
  
  def findWithStatementsAndLevels(id: Long) = {
    DB.withSession{ implicit s=>
      Standards.findWithStatements(id) match {
        case (Some(st),statements) => {
          val returnVal = statements.map( statement => 
            Statements.findWithEducationLevels(statement.id.get)
          )
          (Some(st), returnVal)
        }
        case _ => (None, List((None,List.empty))) 
      }

    }
  }

  def findStatement(id: Long) = {
    DB.withSession{ implicit s=>
      Statements.findWithEducationLevels(id)
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
