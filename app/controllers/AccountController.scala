package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.Logger

case class RegistrationData(firstname: String, lastname: String, email: String, password: String, confirmPassword: String)


trait AccountController {
  this: Controller =>

    val registrationForm = Form(
      mapping(
        "firstname" -> nonEmptyText,
        "lastname" -> nonEmptyText,
        "email" -> email,
        "password" -> text,
        "confirmPassword" ->text 
      )(RegistrationData.apply)(RegistrationData.unapply)
    )

    def create() = Action {
      Ok(views.html.create_account(registrationForm))
    }

    def createPost() = Action { implicit request =>
      registrationForm.bindFromRequest.fold(
        formWithErrors => {
          Logger.error("There was an error in the form")
          Logger.error(formWithErrors.toString) 
          BadRequest(views.html.create_account(formWithErrors))
        },
        registrationData => {
          Logger.info("No errors!")
          Redirect(routes.ApplicationController.index())
        }
      )
    }
}

object AccountController extends Controller with AccountController
