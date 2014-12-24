package services;

import play.api.Logger
import models._
import play.api.db.slick.DB
import play.api.Play.current

object QuestionsService {

  def create(q: Question) = {
    DB.withSession{ implicit s=>
      val newQuestion = Questions.create(q)
      if(newQuestion.id == None)
        None
      Some(newQuestion)
    }
  }

  def create(q: JsonQuestion, statements: List[Statement]) = {
    DB.withSession{ implicit s=>
      // first create the question
      Questions.create(q) match {
        case question: JsonQuestion => {
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
  
 def update(q: Question) = {
    DB.withSession{ implicit s=>
      Questions.update(q)
    }
  }

  def find(id: Long) = {
    DB.withSession{ implicit s=>
      Questions.find(id)
    }
  }

  def findWithStatements(id: Long) = {
    DB.withSession{ implicit s=>
      Questions.findWithStatements(id)
    }
  }

  def all = {
    DB.withSession{ implicit s=>
      Questions.all
    }
  }

  def allWithStatements  = {
    DB.withSession{ implicit s=> 
      Questions.all.map{ q =>
        Questions.findWithStatements(q.id.get)  
      }
    }
  }
}

