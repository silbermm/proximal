package services

import play.api.db.slick.DB
import play.api.Play.current

import collection.mutable.Stack
import scala.concurrent.Future
import org.scalatest._
import org.scalatestplus.play._

import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._


import play.api.Logger
import helpers._


class QuestionsServiceSpec extends PlaySpec with Results {
  import models._
  import QuestionsHelpers._
  import StandardsHelpers._

  val standardsService = new StandardsService() 

  "Questions Service" should {
    
    "create a question with statements" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())){ 
        val s1 =  standardsService.create(fakeStatement) 
        val s2 = standardsService.create(fakeStatement2)
        QuestionsService.create(fakeQuestion, List(s1,s2)) match {
          case (Some(q), statements) => statements must have length fakeStatements.size
          case _ => fail("failure is not an option")
        }
      }
    }
    
    "find a question with statements" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())){ 
        val s1 =  standardsService.create(fakeStatement) 
        val s2 = standardsService.create(fakeStatement2)
        val que = QuestionsService.create(fakeQuestion, List(s1,s2))
        QuestionsService.findWithStatements(que._1.get.id.get) match {
          case (Some(q), statementList) => statementList must have length 2
          case _ => fail("your such a failure")
        }
      }
    }

    "find all questions with statements" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())){ 
        val s1 =  standardsService.create(fakeStatement) 
        val s2 = standardsService.create(fakeStatement2)
        val que = QuestionsService.create(fakeQuestion, List(s1,s2))
        QuestionsService.allWithStatements match {
          case res: List[(Option[Question], List[Statement])] => {
            res must have length 1 
            res.head._2 must have length 2
          }
          case _ => fail("you failed me for the last time!")
        } 
      }
    }
  }
}
