package controllers

import play.api._
import play.api.mvc._

trait ApplicationController {
  this: Controller =>

  def index() = Action {
    Ok(views.html.index()) 
  }

}

object ApplicationController extends Controller with ApplicationController
