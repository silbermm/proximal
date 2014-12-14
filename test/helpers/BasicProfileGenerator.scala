package helpers

import java.util.Date
import org.scalacheck.Gen
import play.api.libs.json._
import securesocial.core.{AuthenticationMethod, BasicProfile}
import models._
import play.Logger

object BasicProfileGenerator {
  val nameGen = for {
      head <- Gen.alphaUpperChar
      size<- Gen.choose(1,10)
      tail <- Gen.listOfN(size,Gen.alphaLowerChar)
  } yield (head +: tail).mkString("")

  def authMethodGen = Gen.oneOf(AuthenticationMethod.OAuth1,AuthenticationMethod.OAuth2,AuthenticationMethod.OpenId,AuthenticationMethod.UserPassword)

  def authMethod = authMethodGen.sample.get
  def userId = nameGen.sample.get
  def providerId= nameGen.sample.get

  def basicProfileGen(userId:String, providerId:String,authMethod: AuthenticationMethod=authMethod)= for{
    firstName <- nameGen
    lastName <- nameGen
    email = s"${firstName.head}.$lastName@example.com"
  }yield BasicProfile(userId,providerId, Some(firstName), Some(lastName), Some(s"$firstName $lastName"),Some(email),None, authMethod,None, None, None)

  def basicProfile(userId:String=userId, providerId:String=providerId,authMethod: AuthenticationMethod=authMethod)=basicProfileGen(userId,providerId, authMethod).sample.get

}

object ChildGenerator {


  implicit val edlevelFormater = Json.format[EducationLevel]

  val nameGen = for {
      head <- Gen.alphaUpperChar
      size<- Gen.choose(1,10)
      tail <- Gen.listOfN(size,Gen.alphaLowerChar)
  } yield (head +: tail).mkString("")
  
  def child(edLevel: EducationLevel) : JsValue = { 
    val json = Json.obj("firstName" -> nameGen.sample.get, "lastName"->nameGen.sample.get, "birthDate"-> (new Date()).getTime(), "educationLevel" -> Json.toJson(edLevel)) 
    Logger.debug( json.toString())
    return json
  }
}
