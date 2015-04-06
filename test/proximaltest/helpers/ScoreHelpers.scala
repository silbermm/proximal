package proximaltest.helpers

import models._
import org.scalacheck.Arbitrary

object ScoreHelpers {
  def scoreGen = for {
    studentId <- Arbitrary.arbitrary[Long]
    questionId <- Arbitrary.arbitrary[Long]
    actId <- Arbitrary.arbitrary[Long]
    score <- Arbitrary.arbitrary[Long]
    timestamp <- Arbitrary.arbitrary[Long]
  } yield Score(None, studentId, Some(questionId), Some(actId), None, Some(score), timestamp)

  val sampleScore = scoreGen.sample.get
}
