package controllers

import play.api.mvc.{ Controller, RequestHeader }
import play.api.templates.{ Txt }
import play.twirl.api.Html
import securesocial.core.{ BasicProfile, RuntimeEnvironment }
import play.api.data.Form
import play.api.i18n.Lang
import securesocial.controllers._
import models._

class CustomTemplates(env:RuntimeEnvironment[_]) extends ViewTemplates {
  
  implicit val implicitEnv = env

  override def getLoginPage(form: Form[(String, String)],msg: Option[String] = None)(implicit request: RequestHeader, lang: Lang): Html = {
    views.html.login(form, msg)(request, env)
  }
  
  override def getSignUpPage(form: Form[RegistrationInfo], token: String)(implicit request: RequestHeader, lang: Lang): Html = {
    views.html.registration.signup(form, token)(request, lang, env)
  }

  override def getStartSignUpPage(form: Form[String])(implicit request: RequestHeader, lang: Lang): Html = {
    views.html.registration.startSignup(form)(request, lang, env) 
  }

  override def getStartResetPasswordPage(form: Form[String])(implicit request: RequestHeader, lang: Lang): Html = {
    views.html.registration.startResetPassword(form)(request, lang, env)
  }

  override def getResetPasswordPage(form: Form[(String, String)], token: String)(implicit request: RequestHeader, lang: Lang): Html = {
    securesocial.views.html.Registration.resetPasswordPage(form, token)(request, lang, env)
  }

  override def getPasswordChangePage(form: Form[ChangeInfo])(implicit request: RequestHeader, lang: Lang): Html = {
    securesocial.views.html.passwordChange(form)(request, lang, env)
  }

  override def getNotAuthorizedPage(implicit request: RequestHeader, lang: Lang): Html = {
    securesocial.views.html.notAuthorized()(request, lang, env)
  }

}