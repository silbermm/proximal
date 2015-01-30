package helpers

import play.api.libs.json._
import models._
import services._

object ImplicitJsonFormat {
  implicit val scoresFormat = Json.format[Score]
  implicit val personFormat = Json.format[Person] 
  implicit val standardFormat = Json.format[Standard]
  implicit val statementFormat = Json.format[Statement]
  implicit val questionFormat = Json.format[JsonQuestion]
  implicit val scoresExtendedFormat = Json.format[ScoreWithQuestionAndStudent]
  implicit val questionScoresFormat = Json.format[QuestionScore]
  implicit val assesmentFormat = Json.format[Assesment]
  implicit val assesmentQuestionScoreFormat = Json.format[AssesmentQuestionScore]
  implicit val assessmentQuestionFormat = Json.format[AssessmentQuestion]
}
