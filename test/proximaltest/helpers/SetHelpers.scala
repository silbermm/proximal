package proximaltest.helpers

import models._
import org.scalacheck.Arbitrary

object SetHelpers {

  def setGen = for {
    title <- Arbitrary.arbitrary[String]
    description <- Arbitrary.arbitrary[String]
  } yield Set(None, Some(title), Some(description))

  val sampleSet = setGen.sample.get

}
