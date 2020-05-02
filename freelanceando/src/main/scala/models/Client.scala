package models

import org.json4s.JValue
import org.json4s.JString
import org.json4s.DefaultFormats


/* Object companion of class Category */
object Client extends ModelCompanion[Client] {

  implicit lazy val formats = DefaultFormats

  /* This class constructor needs to be overwritten in every subclass of
   * ClientCompanion, because it needs the direct reference to each subclass,
   * in this case, Client.
   */
  def apply: Client = new Client
}



class Client extends Model[Client] {

  // TODO complete here with the methods for your model
  // Attributes
  protected[models] var username: String = "DefaultStr"
  protected[models] var country_code: String = "DefaultStr"  

  override def fromJson(jsonValue: JValue): Client = {
    // TODO Parse jsonValue here and assign the values to
    // the instance attributes.
    super.fromJson(jsonValue)
    (jsonValue \ "username") match {
      case JString(value) => username = value.toString
      case _ =>  // Do nothing, things may not have an id
    }
    (jsonValue \ "country_code") match {
      case JString(value) => country_code = value.toString
      case _ =>  // Do nothing, things may not have an id
    }
    this  // Return a reference to this object.
  }
  
  override def toMap: Map[String, Any] = {
    super.toMap + ("username" -> username, "country_code"-> country_code)
  }
  
}
