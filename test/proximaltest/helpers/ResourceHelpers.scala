package proximaltest.helpers

import models._
import java.util.Date
import org.scalacheck.Gen
import org.scalacheck.Arbitrary
import scala.compat.Platform._

object ResourceHelpers {

  def resourceGen = for {
    id <- Arbitrary.arbitrary[Long]
    description <- Arbitrary.arbitrary[String]
    title <- Arbitrary.arbitrary[String]
    createdOn <- Arbitrary.arbitrary[Long]
    category <- Arbitrary.arbitrary[String]
  } yield Resource(None, title, Some(description), Some(category), None, Some(createdOn))

  val sampleResource = resourceGen.sample.get

}

