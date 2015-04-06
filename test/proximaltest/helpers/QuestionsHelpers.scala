package proximaltest.helpers

import models._
import org.scalacheck.Arbitrary

object QuestionsHelpers {

  def questionGen = for {
    text <- Arbitrary.arbitrary[String]
    answer <- Arbitrary.arbitrary[String]
  } yield Question(None, text, None, Some(answer), None)

  val sampleQuestion = questionGen.sample.get

  def fakeQuestion: Question = {
    new Question(None, "What is the first letter of the alphabet?", None, Some("A"), None)
  }

}

