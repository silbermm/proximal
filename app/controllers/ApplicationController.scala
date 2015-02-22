package controllers

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

import java.io.File
import play.api._
import play.api.mvc._
import play.api.Play.current
import services._

import play.api.libs.json._
import play.api.Logger
import securesocial.core._
import models._
import org.apache.commons.codec.binary.Base64

import play.api.libs.concurrent.Akka
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration._
import scala.concurrent.Future

import helpers.ImplicitJsonFormat._

class ApplicationController(override implicit val env: RuntimeEnvironment[SecureUser]) extends securesocial.core.SecureSocial[SecureUser] {

  val uploadActor = Akka.system.actorOf(Props[UploadActor])
  implicit val timeout = Timeout(30 seconds)

  def index = SecuredAction { implicit request =>
    Ok(views.html.index(request.user))
  }

  def upload = SecuredAction.async(parse.multipartFormData) { implicit request =>

    def createUpload(u: Upload): Future[Result] = {
      ask(uploadActor, CreateUpload(u)).mapTo[Option[Upload]] map { x =>
        x match {
          case Some(up) => Ok(Json.toJson(up))
          case None => BadRequest(Json.obj("message" -> "Sorry, unable to create the upload"))
        }
      }
    }

    request.body.file("file").map { file =>
      Logger.debug(file.ref.file.getCanonicalPath())
      val path: Path = Paths.get(file.ref.file.getCanonicalPath());
      val data = Files.readAllBytes(path);
      val base64 = Base64.encodeBase64String(data);
      val upload = Upload(None, base64, file.contentType, Some(file.filename))
      createUpload(upload)
    }.getOrElse {
      Future { BadRequest(Json.obj("message" -> "nope")) }
    }
  }

}

