import play.api._
import play.api._
import play.api.mvc._
import play.api.mvc.Results._
import scala.concurrent.Future
import java.lang.reflect.Constructor
import securesocial.core.RuntimeEnvironment
import services._
import models._

object Global extends GlobalSettings {
  override def onStart(app: Application) {
    Logger.info("Application has started")
  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
  }
  
  override def onError(request: RequestHeader, ex: Throwable) = {
    Future.successful(InternalServerError(
        views.html.error(ex)
    ))
  }

  override def onHandlerNotFound(request: RequestHeader) = {
    Future.successful(NotFound(
      views.html.notFoundPage(request.path)
    ))
  }

  override def onBadRequest(request: RequestHeader, error: String) = {
    Future.successful(BadRequest("Bad Request: " + error))
  }

 
  /**
   * * The runtime environment for this sample app.
   * */
  object MyRuntimeEnvironment extends RuntimeEnvironment.Default[SecureUser] {
    //override lazy val routes = new CustomRoutesService()
    override lazy val userService: SecureUserService = new SecureUserService()
    //override lazy val eventListeners = List(new MyEventListener())
  }

   override def getControllerInstance[A](controllerClass: Class[A]): A = {
     val instance = controllerClass.getConstructors.find { c =>
       val params = c.getParameterTypes
       params.length == 1 && params(0) == classOf[RuntimeEnvironment[SecureUser]]
     }.map {
       _.asInstanceOf[Constructor[A]].newInstance(MyRuntimeEnvironment)
     }
     instance.getOrElse(super.getControllerInstance(controllerClass))
   }

}
