package helpers

import models._
import java.util.Date

object QuestionsHelpers {
  
  def fakeQuestion : Question = {
    new Question(None,"What is the first letter of the alphabet?", None, None, Some("A"))
  }
}


