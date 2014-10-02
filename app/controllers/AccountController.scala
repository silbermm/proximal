package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.Logger
import models._
import play.api.data.validation._
import play.api.db.slick.DB
import play.api.Play.current
import org.mindrot.jbcrypt._

case class RegistrationData(firstname: String, lastname: String, email: String, password: String, confirmPassword: String)

trait AccountController {
  this: Controller =>
  
  def validatePasswordsMatch(firstname: String, lastname: String, email: String, password: String, confirmPassword: String) = {
    if(password == confirmPassword){
      Some(RegistrationData(firstname: String, lastname: String, email: String, password: String, confirmPassword: String))
    }else {
      None
    }
  }
  
  
  val emailUniqunessConstraint: Constraint[String] = Constraint("constraints.emailuniquness")({
      plainText => 
        DB.withSession{ implicit s => 
          Users.findByEmail(plainText) match {
            case Some(u) => Invalid("Email is already in use")
            case None    => Valid
          }
        }
  })

  val emailCheck : Mapping[String] = email.verifying(emailUniqunessConstraint)

  val registrationForm = Form(
      mapping(
        "firstname" -> nonEmptyText,
        "lastname" -> nonEmptyText,
        "email" -> emailCheck,
        "password" -> text,
        "confirmPassword" ->text
        )(RegistrationData.apply)(RegistrationData.unapply) verifying("Passwords do not match", fields => fields match {
              case registrationData => validatePasswordsMatch(registrationData.firstname,registrationData.lastname,registrationData.email,registrationData.password,registrationData.confirmPassword).isDefined
        })
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
          DB.withSession { implicit s => 
            val u: User = new User(None, registrationData.firstname, 
                                registrationData.lastname, registrationData.email, 
                                BCrypt.hashpw(registrationData.password, BCrypt.gensalt()), None)
            try {
              Users.insert(u) 
              Redirect(routes.ApplicationController.index())
            } catch {
              case e: Exception => BadRequest(views.html.create_account(registrationForm))   
            }      
          }
          
        }
      )
    }
}

object AccountController extends Controller with AccountController
