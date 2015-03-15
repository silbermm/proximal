package proximaltest.helpers

import models._
import java.util.Date
import org.scalacheck.Gen
import org.scalacheck.Arbitrary
import scala.compat.Platform._

object SetHelpers {

  def setGen = for {
    title <- Arbitrary.arbitrary[String]
    description <- Arbitrary.arbitrary[String]
  } yield Set(None, Some(title), Some(description))

  val sampleSet = setGen.sample.get

}
