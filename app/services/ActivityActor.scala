package services

import akka.actor.Actor
import models._
import play.api.Play.current
import play.api.db.slick.DB
import play.Logger
import scala.util.Random

case class CreateHomeworkActivity(statementIds: List[Long], activity: Activity, homework: Homework, acts: List[Act])
case class ListHomework(studentId: Long)
case class DeleteHomework(uid: Long, homeworkId: Long)

case class CreateActivity(activity: Activity, statementIds: List[Long])
case class ListActivities(uid: Long)
case class DeleteActivity(activityId: Long)

case class FindStatement(activityId: Long)
case class FindActivities(studentId: Long, category: String)

case class GetQuestion(studentId: Long, standardId: Long)
case class CreateActivityAttempt(studentId: Long, activityId: Long, score: Int)

case class ActivityQuestion(activity: Activity, question: QuestionWithPicture)

class ActivityActor extends Actor {

  def receive = {
    case ca: CreateActivity => {
      try {
        val newActivity = ActivityActor.createActivity(ca)
        sender ! newActivity
      } catch {
        case e: Throwable => {
          sender ! akka.actor.Status.Failure(e)
          throw e
        }
      }
    }
    case fa: FindActivities => {
      val activities = ActivityActor.findActivities(fa.studentId, fa.category, None)
      sender ! activities
    }
    case getQuestion: GetQuestion => {
      val question = ActivityActor.getQuestion(getQuestion.studentId, getQuestion.standardId)
      sender ! question
    }
    case la: ListActivities => {
      try {
        val activities = ActivityActor.listActivities(la.uid)
        sender ! activities
      } catch {
        case e: Throwable => {
          sender ! akka.actor.Status.Failure(e)
          throw e
        }
      }
    }
    case la: DeleteActivity => {
      try {
        val activities = ActivityActor.deleteActivity(la.activityId)
        sender ! activities
      } catch {
        case e: Throwable => {
          sender ! akka.actor.Status.Failure(e)
          throw e
        }
      }
    }
    case fs: FindStatement => {
      sender ! ActivityActor.findStatement(fs.activityId)
    }
    case cha: CreateHomeworkActivity => {
      val newActivity = ActivityActor.createHomework(cha)
      sender ! newActivity
    }
    case student: ListHomework => {
      val allHomework = ActivityActor.listHomework(student.studentId)
      sender ! allHomework
    }
    case activity: DeleteHomework => {
      val deleted = ActivityActor.deleteHomework(activity.uid, activity.homeworkId)
      sender ! deleted
    }
    case _ => sender ! None
  }

}

object ActivityActor {

  private val random = new Random

  def deleteHomework(uid: Long, homeworkId: Long) = {
    DB.withSession { implicit s =>
      Homeworks.find(homeworkId) match {
        case Some(h) => {
          PersonService.isChildOf[Int](uid, h.studentId.get, c => {
            Activities.find(h.activityId.get) match {
              case Some(a) => {
                ActivityActs.deleteByActivity(a.id.get)
                ActivityStatements.deleteByActivity(a.id.get)
                Homeworks.delete(h)
                val deleted = Activities.delete(a)
                Some(deleted)
              }
              case _ => None
            }
          })
        }
        case _ => 0
      }
    }
  }

  def findStatement(activityId: Long): List[Statement] = {
    DB.withSession { implicit s =>
      Activities.findWithStatements(activityId) map (_.statements) getOrElse (List.empty)
    }
  }

  def findActivities(studentId: Long, category: String, standardId: Option[Long]): List[Activity] = {
    DB.withSession { implicit s =>
      val standards = standardId map { x =>
        Standards.find(x) match {
          case Some(stan) => List(stan)
          case None => Standards.list
        }
      } getOrElse Standards.list
      // for each standard,  find all study activities in the students grade level
      for {
        standard <- standards
        activities <- Activities.filterByStandardLevelCategory(studentId, standard.id.get, category)
      } yield activities
    }
  }

  def getStudyActivity(studentId: Long, standardId: Long): Option[Activity] = {
    DB.withSession { implicit s =>
      val activities = filterOutAttemptedActivities(
        findActivities(studentId, "study", Some(standardId)),
        studentId)
      val releventActivities = filterReleventActivities(10, activities, standardId, studentId)
      if (releventActivities.length == 0) {
        Some(activities(random.nextInt(activities.length)))
      } else {
        Logger.debug(s"releventActivities: ${releventActivities.map(_.title.get) + "\n"}")
        val act = releventActivities(random.nextInt(releventActivities.length))
        Some(act)
      }
    }
  }

  def getQuestion(studentId: Long, standardId: Long): Option[ActivityQuestion] = {
    DB.withSession { implicit s =>
      val activities = filterOutAttemptedActivities(
        findActivities(studentId, "question", Some(standardId)),
        studentId)
      //pick random for now...
      val releventActivities = filterReleventActivities(10, activities, standardId, studentId)
      Logger.debug(s"releventActivities: ${releventActivities.map(_.title.get) + "\n"}")
      if (releventActivities.length == 0) {
        val activity = activities(random.nextInt(activities.length))
        convertToActivityQuestion(activity)
      } else {
        val activity = releventActivities(random.nextInt(releventActivities.length))
        convertToActivityQuestion(activity)
      }
    }
  }

  def convertToActivityQuestion(activity: Activity): Option[ActivityQuestion] = {
    DB.withSession { implicit s =>
      val question: Option[QuestionWithPicture] = activity.resourceId map (rid =>
        Questions.findByResourceId(rid) map { q =>
          Questions.findWithPicture(q.id.get)
        } getOrElse None
      ) getOrElse None
      question map { q =>
        ActivityQuestion(activity, q)
      }
    }
  }

  def filterOutAttemptedActivities(activities: List[Activity], studentId: Long) = {
    DB.withSession { implicit s =>
      activities.filter(activity => {
        activity.id map { aid =>
          Attempts.findByStudentAndActivity(studentId, aid) match {
            case Some(activ) => false
            case _ => true
          }
        } getOrElse true
      })
    }
  }

  def filterReleventActivities(howMany: Int, activities: List[Activity], standardId: Long, studentId: Long): List[Activity] = {
    val lastFour = lastNumberOfAttempts(howMany, studentId, standardId)
    if (lastFour.length < 1) return activities
    if (lastFour.length < howMany) {
      val lastStatementAsked = getStatement(lastFour(lastFour.length - 1).activityId) getOrElse (throw new Exception("the last thing asked didn't have a statement!"))
      activities.filter(activity => {
        getStatement(activity.id.get) map { statement =>
          (statement.sequence getOrElse 0L) == (lastStatementAsked.sequence getOrElse 0L) ||
            (statement.sequence getOrElse 0L) == (lastStatementAsked.sequence getOrElse 0L) - 1 ||
            (statement.sequence getOrElse 0L) == (lastStatementAsked.sequence getOrElse 0L) + 1
        } getOrElse false
      })
    } else {
      val (right, wrong) = averageTheAttempts(lastFour);
      Logger.debug(s"average difficulty of right: $right");
      Logger.debug(s"average difficulty of wrong: $wrong");
      // return something in the middle....
      Logger.debug(s"returning activities with difficulty ${right + wrong / 2}")
      activities.filter(activity => {
        getStatement(activity.id.get) map { statement =>
          statement.sequence == (right + wrong / 2)
        } getOrElse false
      })
    }
  }

  def getStatement(activityId: Long): Option[Statement] = {
    DB.withSession { implicit s =>
      Activities.findWithStatements(activityId) map { activityWithStatements =>
        if (activityWithStatements.statements.length < 1) None else Some(activityWithStatements.statements(0))
      } getOrElse None
    }
  }

  def lastNumberOfAttempts(number: Int, studentId: Long, standardId: Long): List[Attempt] = {
    DB.withSession { implicit s =>
      def lastNumber(attempts: List[Attempt]): List[Attempt] =
        if (attempts.length <= number) return attempts else lastNumber(attempts.tail)

      val myAttempts = Attempts.findByStudentAndStandard(studentId, standardId)
      lastNumber(myAttempts)
    }
  }

  def averageTheAttempts(attempts: List[Attempt]): (Int, Int) = {
    val gotRight = attempts.filter(_.score == 5)
    val totalDifficulty = gotRight.foldLeft(0L) { (counter, current) =>
      val difficulty: Long = getStatement(current.activityId) map (_.sequence getOrElse 0L) getOrElse 0L
      counter + difficulty
    }
    val gotWrong = attempts.filter(_.score == 3)
    val totalWrongDifficulty = gotRight.foldLeft(0L) { (counter, current) =>
      val difficulty: Long = getStatement(current.activityId) map (_.sequence getOrElse 0L) getOrElse 0L
      counter + difficulty
    }
    val averageDifficultyOfRight: Int = totalDifficulty.toInt / gotRight.length.toInt
    val averageDifficultyOfWrong: Int = totalWrongDifficulty.toInt / gotWrong.length.toInt
    (averageDifficultyOfRight, averageDifficultyOfWrong)
  }

  def createActivity(ca: CreateActivity): Option[Activity] = {
    DB.withSession { implicit s =>
      val realCreator = People.findPersonByUid(ca.activity.creator).get
      val newActivity = Activities.create(ca.activity.copy(creator = realCreator.id.get))
      val activityStatements = ca.statementIds map (sid =>
        ActivityStatements.create(ActivityStatement(None, newActivity.id.get, sid))
      )
      Some(newActivity)
    }
  }

  def listActivities(uid: Long): List[Activity] = {
    DB.withSession { implicit s =>
      if (uid == 0) {
        Activities.all
      } else {
        People.findPersonByUid(uid) match {
          case Some(person) => Activities.allByPerson(person.id.get)
          case _ => List.empty
        }
      }
    }
  }

  def deleteActivity(id: Long) = {
    DB.withSession { implicit s =>
      val activity = Activities.find(id);
      ActivityStatements.deleteByActivity(id);
      Activities.delete(activity.get);
    }
  }

  def createHomework(homeworkActivity: CreateHomeworkActivity): Option[CreateHomeworkActivity] = {
    DB.withSession { implicit s =>
      homeworkActivity.activity.id match {
        case Some(identifier) => {
          //don't create a new activity, it already exists
          //TODO: Update said activity
          //TODO: Update Acts as well
          val activityStatements = homeworkActivity.statementIds map (ActivityStatement(None, identifier, _))
          val createdActivityStatements: List[Long] = createActivityStatements(activityStatements) map (_.statementId)
          val newHomework = Homeworks.create(homeworkActivity.homework)
          Some(CreateHomeworkActivity(createdActivityStatements, homeworkActivity.activity, newHomework, homeworkActivity.acts))
        }
        case None => {
          //create a new activity as well as the homework
          val newActivity = Activities.create(homeworkActivity.activity)
          val acts = handleActs(homeworkActivity.acts)
          createActivityActs(newActivity, acts)
          val activityStatements = homeworkActivity.statementIds map (ActivityStatement(None, newActivity.id.get, _))
          val createdActivityStatements: List[Long] = createActivityStatements(activityStatements) map (_.statementId)
          val homework = homeworkActivity.homework.copy(activityId = newActivity.id)
          val newHomework = Homeworks.create(homework)
          Some(CreateHomeworkActivity(createdActivityStatements, newActivity, newHomework, acts))
        }
      }
    }
  }

  def listHomework(studentId: Long): List[HomeworkActivityActs] = {
    DB.withSession { implicit s =>
      Homeworks.findByStudentWithActivitiesAndActs(studentId)
    }
  }

  def handleActs(acts: List[Act]): List[Act] = {
    DB.withSession { implicit s =>
      val actsToCreate = for { act <- acts if act.id.isEmpty } yield act
      val actsToUpdate = for { act <- acts if !act.id.isEmpty } yield act
      val createdActs = for { act <- actsToCreate } yield Acts.create(act)
      val updatedActs = for { act <- actsToUpdate } yield Acts.update(act)
      createdActs ::: updatedActs
    }
  }

  def createActivityActs(activity: Activity, acts: List[Act]) = {
    DB.withSession { implicit s =>
      for {
        act <- acts
      } ActivityActs.create(ActivityAct(None, activity.id.get, act.id.get))
    }
  }

  def createActivityStatements(ast: List[ActivityStatement]) = {
    DB.withSession { implicit s =>
      for {
        actStatements <- ast
      } yield (ActivityStatements.create(actStatements))
    }
  }
}

