package services;

import play.api.Logger
import models._
import play.api.db.slick.DB
import play.api.Play.current

trait QuestionsService {
  def create(q: Question) : Option[Question]
  def create(q: Question, statements: List[Statement]) : (Option[Question], List[Statement]) 
  def update(q: Question) : Int
  def find(id: Long) : Option[Question]
  def findWithStatements(id: Long) : (Option[Question], List[Statement])
  def all : List[Question]
  def allWithStatements : List[(Option[Question], List[Statement])]
}

class QuestionsDBService extends QuestionsService {
  
  override def create(q: Question) = {
    DB.withSession{ implicit s=>
      val newQuestion = Questions.create(q)
      if(newQuestion.id == None)
        None
      Some(newQuestion)
    }
  }

  override def create(q: Question, statements: List[Statement]) = {
    DB.withSession{ implicit s=>
      // first create the question
      Questions.create(q) match {
        case question: Question => {
          Logger.debug(s"statements list = $statements.toList" )
          val qs = for (
            st <- statements
          ) yield QuestionWithStatements(None, question.id.get,st.id.get); 
          for ( st <- qs ) QuestionsWithStatements.create(st)
          Questions.findWithStatements(question.id.get)    
        }
        case _ => (None, List.empty)
      }
    }
  }

  override def update(q: Question) = {
    DB.withSession{ implicit s=>
      Questions.update(q)
    }
  }

  override def find(id: Long) = {
    DB.withSession{ implicit s=>
      Questions.find(id)
    }
  }

  override def findWithStatements(id: Long) = {
    DB.withSession{ implicit s=>
      Questions.findWithStatements(id)
    }
  }

  override def all = {
    DB.withSession{ implicit s=>
      Questions.all
    }
  }

  override def allWithStatements  = {
    DB.withSession{ implicit s=> 
      Questions.all.map{ q =>
        Questions.findWithStatements(q.id.get)  
      }
    }
  }

}
