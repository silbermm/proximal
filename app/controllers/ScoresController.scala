package controllers

import play.api._
import play.api.mvc._
import services._
import securesocial.core._
import models._
import play.api.libs.json._
import play.api.libs.functional.syntax._

class ScoresController(override implicit val env: RuntimeEnvironment[SecureUser]) extends securesocial.core.SecureSocial[SecureUser] {
  
  def showScores(studentId: Long) = SecuredAction{ implicit request =>
    
  }
   

}
