/* Copyright 2015 Matt Silbernagel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package services

import akka.actor.Actor
import models._
import play.api.Logger
import play.api.Play.current
import play.api.db.slick.DB

case class CreateUpload(upload: Upload)
case class DeleteUpload(upload: Upload)
case class FindUpload(uploadId: Int)
case class AllUploads()

class UploadActor extends Actor {

  override def receive = {
    case upload: CreateUpload => {
      val uploaded = createUpload(upload.upload)
      sender ! uploaded
    }
  }

  private def createUpload(u: Upload): Option[Upload] = {
    DB.withSession { implicit s =>
      try {
        val up = Uploads.create(u)
        Some(up)
      } catch {
        case ex: Throwable => {
          Logger.debug(s"$ex")
          None
        }
      }
    }
  }
}

