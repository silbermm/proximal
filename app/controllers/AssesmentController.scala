package controllers

import play.api._
import play.api.mvc._
import securesocial.core._
import models._
import services.AssesmentService
import helpers.RolesHelper
import play.api.Logger
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.db.slick.DB
import play.api.Play.current
import helpers.ImplicitJsonFormat._

class AssesentsController(override implicit val env: RuntimeEnvironment[SecureUser]) extends securesocial.core.SecureSocial[SecureUser] {

  def newAssesment(childId: Long) = SecuredAction { implicit request =>
    // Make sure the logged in user is allowed to interact with this child
    
  }

}
