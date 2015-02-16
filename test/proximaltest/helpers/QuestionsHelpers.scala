package proximaltest.helpers

import models._
import java.util.Date
import org.scalacheck.Gen
import org.scalacheck.Arbitrary

object QuestionsHelpers {

  def questionGen = for {
    text <- Arbitrary.arbitrary[String]
    answer <- Arbitrary.arbitrary[String]
  } yield Question(None, text, None, None, Some(answer))

  def fakeQuestion: Question = {
    new Question(None, "What is the first letter of the alphabet?", None, None, Some("A"))
  }

}

