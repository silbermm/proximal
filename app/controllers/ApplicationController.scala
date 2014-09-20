package controllers

import play.api._
import play.api.mvc._

trait ApplicationController {
  this: Controller =>

  def index() = Action {
    Ok(views.html.index()) 
  }

  def login() = Action {
    Ok(views.html.login())
  }
}

object ApplicationController extends Controller with ApplicationController
