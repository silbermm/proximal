package helpers

import play.api.libs.json._
import models._
import services._
import securesocial.core._

object ImplicitJsonFormat {
  implicit val scoresFormat = Json.format[Score]
  implicit val personFormat = Json.format[Person]
  implicit val standardFormat = Json.format[Standard]
  implicit val statementFormat = Json.format[Statement]

  implicit val uploadsFormat = Json.format[Upload]
  implicit val questionFormat = Json.format[JsonQuestion]
  implicit val scoresExtendedFormat = Json.format[ScoreWithQuestionAndStudent]
  implicit val questionScoresFormat = Json.format[QuestionScore]
  implicit val assesmentFormat = Json.format[Assesment]
  implicit val assesmentQuestionScoreFormat = Json.format[AssesmentQuestionScore]
  implicit val assessmentQuestionFormat = Json.format[AssessmentQuestion]

  implicit val edLevelFormat = Json.format[EducationLevel]
  implicit val childFormat = Json.format[Child]
  implicit val peopleFormat = Json.format[Person]
  implicit val authMethodFormat = Json.format[securesocial.core.AuthenticationMethod]
  implicit val oAuth1Format = Json.format[securesocial.core.OAuth1Info]
  implicit val oAuth2Format = Json.format[securesocial.core.OAuth2Info]
  implicit val passwordInfoFormat = Json.format[securesocial.core.PasswordInfo]

  implicit val secureUserFormat = Json.format[SecureUser]
  implicit val roleFormat = Json.format[Role]
  implicit val profileFormat = Json.format[Profile]

  implicit val actFormat = Json.format[Act]
  implicit val activityFormat = Json.format[Activity]
  implicit val homeworkFormat = Json.format[Homework]
  implicit val studyFormat = Json.format[Study]

  implicit val createHomeworkFormat = Json.format[CreateHomeworkActivity]
  implicit val homeworkActivityActsFormat = Json.format[HomeworkActivityActs]

}
