package proximaltest.helpers;

import controllers.ChildAndActivity
import org.scalatestplus.play._
import play.api.libs.json._
import play.api.test.Helpers._
import play.api.test.{ FakeApplication, FakeRequest }
import play.api.Play.current
import play.api.db.slick.DB
import models._

object Fixtures {

  def setupUserAndChild = {
    val creds1 = cookies(route(FakeRequest(POST, "/authenticate/naive").withTextBody("user")).get)
    val Some(user) = route(FakeRequest(GET, "/api/v1/profile").withCookies(creds1.get("id").get))
    val json: JsValue = Json.parse(contentAsString(user))
    val parentId = (json \ "user" \ "uid").as[Long]
    val e = DB.withSession { implicit s => EducationLevels.insert(StandardsHelpers.fakeEducationLevel) }
    val resp = route(FakeRequest(POST, "/api/v1/children").withCookies(creds1.get("id").get).withJsonBody(ChildGenerator.child(e))).get
    val studentJson = Json.parse(contentAsString(resp))
    val childId = (studentJson \ "id").as[Long]
    (parentId, childId, e, creds1)
  }

  def setupStandardsAndStatements(edLevel: EducationLevel) = {
    DB.withSession { implicit s =>
      val standard = Standards.insert(StandardsHelpers.fakeStandard)
      val statement = Statements.insert(StandardsHelpers.fakeStatement.copy(standardId = standard.id))
      val standardLevel = StandardLevels.insert(new StandardLevel(None, edLevel.id.get, standard.id.get))
      val statementLevel = StatementLevels.insert(new StatementLevel(None, edLevel.id.get, statement.id.get))
      (standard, statement)
    }
  }

  def setupActivity(parentId: Long, standard: Standard, statement: Statement, resource: Resource) = {
    DB.withSession { implicit s =>
      val sampleActivity = ActivityHelpers.sampleActivity.copy(creator = parentId, category = Some("question"), resourceId = resource.id)
      val activity = Activities.create(sampleActivity)
      ActivityStatements.create(ActivityStatement(None, activity.id.get, statement.id.get))
      activity
    }
  }

  def setupQuestions(parentId: Long) = {
    DB.withSession { implicit s =>
      val resource = Resources.create(ResourceHelpers.sampleResource.copy(creator = Some(parentId)))
      val question = Questions.create(QuestionsHelpers.sampleQuestion.copy(resourceId = resource.id))
      (resource, question)
    }
  }

}
