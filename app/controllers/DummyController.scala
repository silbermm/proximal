package controllers

import play.api._
import play.api.mvc._
import services._
import securesocial.core._
import models._
import play.api.libs.json._
import play.api.libs.functional.syntax._

class DummyController(override implicit val env: RuntimeEnvironment[SecureUser]) extends securesocial.core.SecureSocial[SecureUser] {

  implicit val dummyFormat = Json.format[Dummy]

  val dummyService: DummyServiceTrait = new DummyService()

  def listDummies = SecuredAction { implicit request =>
    val dummies = dummyService.list
    Ok(Json.toJson(dummies))
  }

}
