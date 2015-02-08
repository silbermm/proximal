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
