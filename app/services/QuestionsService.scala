package services;

import play.api.Logger
import models._
import play.api.db.slick.DB
import play.api.Play.current

trait QuestionsService {
  def create(q: Question) : Option[Question]
  def update(q: Question) : Int
  def find(id: Long) : Option[Question]
  def all : List[Question]
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

  override def all = {
    DB.withSession{ implicit s=>
      Questions.all
    }
  }


}
