package proximaltest.services

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.testkit.TestActorRef
import com.typesafe.config.ConfigFactory
import org.scalatestplus.play._
import play.api.Logger
import play.api.Play.current
import play.api.db.slick.DB
import play.api.mvc._
import play.api.test.Helpers._
import play.api.test._
import proximaltest.helpers._
import securesocial.core.services.SaveMode
import services._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Failure, Success }

class AssessmentActorSpec extends PlaySpec with Results {

  import models._

  var personService = new PersonService()
  var userService = new SecureUserService()
  var standardsService = new StandardsService()

  def withApplication(test: (SecureUser, Person, Person, List[Standard], List[Statement], List[EducationLevel]) => Any) {
    running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      val e = DB.withSession { implicit s => EducationLevels.insert(StandardsHelpers.fakeEducationLevel) }
      val e2 = DB.withSession { implicit s => EducationLevels.insert(StandardsHelpers.fakeEducationLevel2) }
      val edList = List(e, e2)
      val Success(user) = userService.save(BasicProfileGenerator.basicProfile(), SaveMode.LoggedIn).value.get
      val parent = personService.createPerson(PersonHelpers.person.copy(uid = user.uid))
      val child = personService.createPerson(PersonHelpers.child(e.id.get))
      val relationship = personService.addChild(child, parent);

      // Add a fake standard and statements  
      val standard = standardsService.create(StandardsHelpers.fakeStandard)
      val standard2 = standardsService.create(StandardsHelpers.fakeStandard)

      val statement1 = standardsService.create(StandardsHelpers.fakeStatements(0).copy(standardId = standard.id))
      val statement2 = standardsService.create(StandardsHelpers.fakeStatements(1).copy(standardId = standard2.id))

      val standards = List(standard, standard2)
      val statements = List(statement1, statement2)
      DB.withSession { implicit s =>
        StatementLevels.insert(StatementLevel(None, e.id.get, statement1.id.get))
        StatementLevels.insert(StatementLevel(None, e2.id.get, statement2.id.get))
      }
      test(user, parent, child, standards, statements, edList)
    }
  }

  "Assesment Service" should {

    "score the question and ask another question that has not yet been scored" in withApplication { (user, parent, child, standards, statements, edList) =>

      implicit val actorSystem = ActorSystem("testActorSystem", ConfigFactory.load())
      val actorRef = TestActorRef(new AssessmentActor)
      DB.withSession { implicit s =>
        // Create 5 random questions
        (1 to 5) foreach (x => {
          val resource = Resources.create(ResourceHelpers.sampleResource.copy(category = Some("question")))
          val nq = Questions.create(QuestionsHelpers.questionGen.sample.get.copy(resourceId = resource.id))
          val activity = Activities.create(ActivityHelpers.sampleActivity.copy(resourceId = resource.id, creator = parent.id.get))
          ActivityStatements.create(ActivityStatement(None, activity.id.get, statements(0).id.get))
        })
      }
      ask(actorRef, CreateAssessment(user.uid.get, child.id.get, standards(0).id.get)).mapTo[Option[AssessmentQuestion]] map (message => {
        message match {
          case Some(aq) => {
            val statementSequence: Long = DB.withSession { implicit s =>
              Questions.findActivity(aq.question.id.get) match {
                case Some(act) => {
                  Activities.findWithStatements(act.id.get) map (aWs =>
                    if (aWs.statements.isEmpty) 1L else aWs.statements(0).sequence.getOrElse(1L)
                  ) getOrElse 1L
                }
                case None => 1L
              }
            }
            ask(actorRef, ScoreAssessment(aq.assessment.id.get,
              child.id.get,
              aq.question.id.get,
              standards(0).id.get,
              5))
              .mapTo[Option[AssessmentQuestion]] map (message2 => {
                message2 match {
                  case Some(aq2) => aq2.question.id must not be empty
                  case None => fail("Got an exception")
                  case _ => fail("Didn't get a question back...")
                }
              })
          }
          case None => fail("Got an exception")
          case _ => fail("Didn't get a question back...")
        }
      })

    }

    "start the assessment by asking a random question for this student and ed level" in withApplication { (user, parent, child, standards, statements, edList) =>

      implicit val actorSystem = ActorSystem("testActorSystem", ConfigFactory.load())
      val actorRef = TestActorRef(new AssessmentActor)
      DB.withSession { implicit s =>
        // Create 5 random questions
        (1 to 5) foreach (x => {
          val resource = Resources.create(ResourceHelpers.sampleResource.copy(category = Some("question")))
          val nq = Questions.create(QuestionsHelpers.questionGen.sample.get.copy(resourceId = resource.id))
          val activity = Activities.create(ActivityHelpers.sampleActivity.copy(resourceId = resource.id, creator = parent.id.get))
          ActivityStatements.create(ActivityStatement(None, activity.id.get, statements(0).id.get))
        })

        ask(actorRef, CreateAssessment(user.uid.get, child.id.get, standards(0).id.get)).mapTo[Option[AssessmentQuestion]] map (message => {
          message match {
            case Some(aq) => aq.question.id must not be empty
            case None => fail("Got an exception")
            case _ => fail("Didn't get a question back...")
          }
        })
      }
    }
  }
}
