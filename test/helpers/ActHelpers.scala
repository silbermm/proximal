package helpers

import models._
import java.util.Date
import org.scalacheck.Gen
import org.scalacheck.Arbitrary
import scala.compat.Platform._

object ActHelpers {
  def actGen = for {
    actType <- Arbitrary.arbitrary[String]
    action <- Arbitrary.arbitrary[String]
  } yield Act(None, actType, Some(action), None)

  val sampleAct = actGen.sample.get
}
