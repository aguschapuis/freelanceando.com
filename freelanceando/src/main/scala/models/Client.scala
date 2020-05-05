package models

import org.json4s.{JValue, JInt, JString, DefaultFormats, JNothing}

/* Object companion of class Category */
object Client extends ModelCompanion[Client] {

  implicit lazy val formats = DefaultFormats

  /* This class constructor needs to be overwritten in every subclass of
   * ClientCompanion, because it needs the direct reference to each subclass,
   * in this case, Client.
   */
  def apply: Client = new Client
}



class Client() extends Model[Client] {

  // TODO complete here with the methods for your model
  // Attributes
  protected[models] var username: String = "DefaultStr"
  protected[models] var country_code: String = "DefaultStr"
  protected[models] var total_spend: Int = 0
  
  def getUsername: String = username
  def getCountry_code: String = country_code
  def getTotal_spend: Int = total_spend

  override def fromJson(jsonValue: JValue): Client = {
    // TODO Parse jsonValue here and assign the values to
    // the instance attributes.
    super.fromJson(jsonValue)
    (jsonValue \ "username") match {
      case JNothing =>
      case JString(value) => username = value.toString
      case _ => throw new IllegalArgumentException // Do nothing, things may not have an id
    }
    (jsonValue \ "country_code") match {
      case JNothing =>
      case JString(value) => country_code = value.toString
      case _ => throw new IllegalArgumentException
    }
    (jsonValue \ "total_spend") match {
      case JNothing =>
      case JInt(value) => total_spend = value.toInt
      case _ => throw new IllegalArgumentException
    }
    this  // Return a reference to this object.
  }
  
  override def toMap: Map[String, Any] = {
    super.toMap + ("username" -> username, "country_code"-> country_code)
  }

  def IncrementTotal_spend(amount : Int) : Unit = {
    this.total_spend += amount
  }
  
}
