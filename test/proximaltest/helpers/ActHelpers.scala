package proximaltest.helpers

import models._
import org.scalacheck.Arbitrary

object ActHelpers {
  def actGen = for {
    actType <- Arbitrary.arbitrary[String]
    action <- Arbitrary.arbitrary[String]
    progress <- Arbitrary.arbitrary[String]
  } yield Act(None, actType, Some(action), Some(progress), None)

  val sampleAct = actGen.sample.get
}
