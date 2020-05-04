package models

import org.json4s.{DefaultFormats, JValue, JInt, JString, JArray}
import org.json4s.DefaultFormats


/* Object companion of class Freelancer */
object Freelancer extends ModelCompanion[Freelancer] {

  implicit lazy val formats = DefaultFormats

  /* This class constructor needs to be overwritten in every subclass of
   * ModelCompanion, because it needs the direct reference to each subclass,
   * in this case, Freelancer.
   */
  /* Este constructor de clase debe sobrescribirse en cada subclase de
   * ModelCompanion, porque necesita la referencia directa a cada subclase,
   * en este caso, Freelancer.
   */
  def apply: Freelancer = new Freelancer
}

class Freelancer extends Model[Freelancer] {
  // TODO complete here with the methods for your model

  // Attributes
  protected[models] var username: String = "DefaultStr"
  protected[models] var country_code: String = "DefaultStr"
  protected[models] var category_ids: List[Int] = List()
  protected[models] var reputation: String = "DefaultStr"
  protected[models] var hourly_price: Int = 0

  // Getters
  def getUsername: String = username
  def getCountry_code: String = country_code
  def getCategory_ids: List[Int] = category_ids
  def getReputation: String = reputation
  def getHourly_price: Int = hourly_price

  override def toMap: Map[String, Any] = {
    super.toMap + ("username" -> username,
                  "country_code" -> country_code,
                  "category_ids" -> category_ids,
                  "reputation" -> reputation,
                  "hourly_price" -> hourly_price)
  }
                                            
  
  override def fromJson(jsonValue: JValue): Freelancer = {
    // TODO Parse jsonValue here and assign the values to
    // the instance attributes.
    super.fromJson(jsonValue)
    (jsonValue \ "username") match {
      case JString(value) => username = value.toString
      case _ =>
    }
    (jsonValue \ "country_code") match {
      case JString(value) => country_code = value.toString
      case _ =>
    }
    (jsonValue \ "category_ids") match {
      case JArray(value) => category_ids = value.map { case JInt(x) => x.toInt } // hay que chequear Ids validos?
      case _ =>
    }
    (jsonValue \ "reputation") match {
      case JString(value) => reputation = value.toString
      case _ => reputation = "Junior"
    }
    (jsonValue \ "hourly_price") match {
      case JInt(value) => hourly_price = value.toInt
      case _ =>
    }

    this // Return a reference to this object.
  }
  
  
  
}
