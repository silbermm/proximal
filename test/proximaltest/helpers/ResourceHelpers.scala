package proximaltest.helpers

import models._
import org.scalacheck.Arbitrary

object ResourceHelpers {

  def resourceGen = for {
    id <- Arbitrary.arbitrary[Long]
    description <- Arbitrary.arbitrary[String]
    title <- Arbitrary.arbitrary[String]
    createdOn <- Arbitrary.arbitrary[Long]
    category <- Arbitrary.arbitrary[String]
  } yield Resource(None, Some(title), Some(description), Some(category), None, Some(createdOn))

  val sampleResource = resourceGen.sample.get

}

