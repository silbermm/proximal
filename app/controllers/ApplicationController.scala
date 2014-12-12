package controllers

import play.api._
import play.api.mvc._
import securesocial.core._
import models._


class ApplicationController(override implicit val env: RuntimeEnvironment[SecureUser])  extends securesocial.core.SecureSocial[SecureUser] {

   def index = SecuredAction { implicit request =>
     Ok(views.html.index(request.user))
   }

   

}

