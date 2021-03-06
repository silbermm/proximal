package proximaltest.helpers

import models._
import org.scalacheck.Arbitrary

import scala.compat.Platform._

object ActivityHelpers {

  def activityGen = for {
    id <- Arbitrary.arbitrary[Long]
    creator <- Arbitrary.arbitrary[Long]
    description <- Arbitrary.arbitrary[String]
    rights <- Arbitrary.arbitrary[String]
    source <- Arbitrary.arbitrary[String]
    subject <- Arbitrary.arbitrary[String]
    title <- Arbitrary.arbitrary[String]
    category <- Arbitrary.arbitrary[String]
  } yield Activity(None, creator, currentTime, Some(description), Some(rights), Some(source), Some(subject), Some(title), Some(category), None)

  def homeworkGen = for {
    status <- Arbitrary.arbitrary[String]
  } yield Homework(None, None, None, None, status, currentTime, None)

  val sampleActivity = activityGen.sample.get
  val sampleHomework = homeworkGen.sample.get

}

