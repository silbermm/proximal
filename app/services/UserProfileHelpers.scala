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

package helpers

import models._
import securesocial.core._

object UserProfileHelpers {

  def profileFromUser(user: SecureUser): BasicProfile = {
    BasicProfile(user.userId,
      user.providerId,
      user.firstName,
      user.lastName,
      user.fullName,
      user.email,
      user.avatarUrl,
      user.authMethod,
      user.oAuth1Info,
      user.oAuth2Info,
      user.passwordInfo)
  }

  def fakeUser: SecureUser = {
    val pwordInfo = new PasswordInfo("bcrypt", "9090909090", None)
    val authMethod = new AuthenticationMethod("userPassword")

    new SecureUser(uid = None, providerId = "userpass",
      userId = "silbermm", firstName = Some("Matt"), lastName = Some("Silbernagel"),
      fullName = Some("Matt Silbernagel"), email = Some("silbermm@gmail.com"), avatarUrl = None,
      authMethod = authMethod, oAuth1Info = None, oAuth2Info = None, passwordInfo = Some(pwordInfo))
  }
}
