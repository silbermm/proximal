package helpers

import play.api.libs.json._
import models._

object ImplicitJsonFormat {
  implicit val scoresFormat = Json.format[Score]
  implicit val questionFormat = Json.format[JsonQuestion]
  implicit val personFormat = Json.format[Person]
  implicit val scoresExtendedFormat = Json.format[ScoreWithQuestionAndStudent]

}
