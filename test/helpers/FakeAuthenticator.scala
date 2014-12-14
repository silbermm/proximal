package helpers 

import org.joda.time.DateTime
import play.api.mvc.Result
import play.mvc.Http.Context
import securesocial.core.authenticator.Authenticator
import scala.concurrent.Future

case class FakeAuthenticator[A](id:String, user:A, creationDate: DateTime , expirationDate: DateTime , lastUsed: DateTime) extends Authenticator[A] {

  override def touch: Future[Authenticator[A]] = Future.successful(copy(lastUsed=DateTime.now))
  override def updateUser(user: A): Future[Authenticator[A]] = Future.successful(copy(user=user))
  override def discarding(result:Result): Future[Result] = Future.successful(result)
  override def discarding(javaContext: Context): Future[Unit] = Future.successful(())
  override def isValid: Boolean = true
  override def touching(result: Result): Future[Result] = Future.successful(result)
  override def touching(javaContext: Context): Future[Unit] = Future.successful(())
  override def starting(result: Result): Future[Result] = Future.successful(result)
}

