package proximaltest.helpers

import models._
import java.util.Date
import org.scalacheck.Gen
import org.scalacheck.Arbitrary

object PersonHelpers {

  val nameGen = for {
    head <- Gen.alphaUpperChar
    size <- Gen.choose(1, 10)
    tail <- Gen.listOfN(size, Gen.alphaLowerChar)
  } yield (head +: tail).mkString("")

  def person = Person(None, nameGen.sample.get, nameGen.sample, None, None, None)

  def child(edLevelId: Long) = Person(None, nameGen.sample.get, nameGen.sample, None, Some(edLevelId), None)

}
