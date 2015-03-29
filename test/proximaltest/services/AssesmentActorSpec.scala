package proximaltest.services

import services._

import play.api.db.slick.DB
import play.api.Play.current

import collection.mutable.Stack
import scala.concurrent.Future
import org.scalatestplus.play._

import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._

import scala.compat.Platform

import play.api.Logger
import proximaltest.helpers._

import akka.testkit.TestActorRef
import scala.concurrent.Await
import akka.pattern.ask

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import scala.concurrent.ExecutionContext.Implicits.global
import akka.pattern.ask

import scala.util.{ Try, Success, Failure }

import securesocial.core.services.SaveMode

class AssessmentActorSpec extends PlaySpec with Results {

  import models._

  var personService = new PersonService()
  var userService = new SecureUserService()
  var standardsService = new StandardsService()

  "Assesment Service" should {
    "start the assessment by asking a random question for this student and ed level" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        implicit val actorSystem = ActorSystem("testActorSystem", ConfigFactory.load())
        val actorRef = TestActorRef(new AssessmentActor)

        val e = DB.withSession { implicit s => EducationLevels.insert(StandardsHelpers.fakeEducationLevel) }

        // Generate a parent and student first
        val Success(user) = userService.save(BasicProfileGenerator.basicProfile(), SaveMode.LoggedIn).value.get
        val parent = personService.createPerson(PersonHelpers.person.copy(uid = user.uid))
        val child = personService.createPerson(PersonHelpers.child(e.id.get))
        val relationship = personService.addChild(child, parent);

        // Add a fake standard and statements  
        val standard = standardsService.create(StandardsHelpers.fakeStandard)
        val statement1 = standardsService.create(StandardsHelpers.fakeStatements(0).copy(standardId = standard.id))
        val statement2 = standardsService.create(StandardsHelpers.fakeStatements(1).copy(standardId = standard.id))

        DB.withSession { implicit s =>
          val e1 = StatementLevels.insert(StatementLevel(None, e.id.get, statement1.id.get))
          val e2 = StatementLevels.insert(StatementLevel(None, e.id.get, statement2.id.get))

          (1 to 5) foreach (x => {
            val resource = Resources.create(ResourceHelpers.sampleResource)
            val nq = Questions.create(QuestionsHelpers.questionGen.sample.get.copy(resourceId = resource.id))
            val activity = Activities.create(ActivityHelpers.sampleActivity.copy(resourceId = resource.id, creator = parent.id.get))
          })

          ask(actorRef, CreateAssessment(user.uid.get, child.id.get, standard.id.get)).value map (message => {
            message match {
              case Success(question: JsonQuestion) => question.id must not be empty
              case Failure(ex: Throwable) => fail("Got an exception")
              case _ => fail("Didn't get a question back...")
            }
          })
          //val nxtQ = AssesmentService.nextQuestion(child.id.get)
          //nxtQ.id must not be empty
          //val qscor = Scores.create(Score(None, child.id.get, Some(nxtQ.id.get), None, Some(5), Platform.currentTime))
          //qscor.id must not be empty
          //val nxtQ2 = AssesmentService.nextQuestion(child.id.get)
          //nxtQ.id.get must not be nxtQ2.id.get

        }

      }
    }

  }
}
