package controllers;

import java.lang.reflect.Constructor
import services.SecureUserService
import play.api.GlobalSettings
import play.api.test.{FakeRequest, WithApplication, FakeApplication, PlaySpecification}
import securesocial.core.RuntimeEnvironment

import securesocial.core._
import securesocial.core.services._

import collection.mutable.Stack
import scala.concurrent.Future
import org.scalatest._
import org.scalatestplus.play._

import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._

import play.api.libs.json._

import play.api.Logger
import helpers._

import play.api.db.slick.DB
import play.api.Play.current


class QuestionControllerSpec extends PlaySpec with Results {
  import models._


  "Question Controller" should {
    "not allow a non admin to create a new question" in {
      running(SecureSocialHelper.app){
        val creds1 = cookies(route(FakeRequest(POST, "/authenticate/naive").withTextBody("user")).get)
        val qu = QuestionGenerator.question
        val Some(resp) = route(FakeRequest(POST, "/api/v1/questions").withCookies(creds1.get("id").get).withJsonBody(QuestionGenerator.question))
        status(resp) mustEqual UNAUTHORIZED
      }
    }

    "allow a admin to create a new question" in {
      running(SecureSocialHelper.app) {
        DB.withSession{ implicit s=>
          val creds1 = cookies(route(FakeRequest(POST, "/authenticate/naive").withTextBody("user")).get)
      
        Logger.debug(creds1.get("id").get.toString); 

        //SecureUsers.save(UserProfileHelpers.profileFromUser(creds1.get("id").get), SaveMode.LoggedIn)
          
          val fakeRole = new Role(None,"admin","Administrator of the system")
          val r = Roles.insert(fakeRole)
          r.id must not be empty
          val person = Person( None, creds1.get("firstName").get.value, None, None, None, Some(creds1.get("uid").get.value.toLong))
          val p = People.insertPerson(person) 
          p.id must not be empty
          People.addRole(p,r) mustEqual 1
          val Some(resp) = route(FakeRequest(POST, "/api/v1/questions").withCookies(creds1.get("id").get).withJsonBody(QuestionGenerator.question))
          status(resp) mustEqual OK 
        }
      }
    }
  }

}
