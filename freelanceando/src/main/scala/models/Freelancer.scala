package models

import org.json4s.{DefaultFormats, JValue, JInt, JString, JArray, JNothing}
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
    def JInt2Int(x: JValue): Int = {
      x match {
        case JInt(x) => x.toInt
        case _ => throw new IllegalArgumentException
      }
    }

    // TODO Parse jsonValue here and assign the values to
    // the instance attributes.
    super.fromJson(jsonValue)
    (jsonValue \ "username") match {
      case JNothing =>
      case JString(value) => username = value.toString
      case _ => throw new IllegalArgumentException
    }
    (jsonValue \ "country_code") match {
      case JNothing =>
      case JString(value) => country_code = value.toString
      case _ => throw new IllegalArgumentException
    }
    (jsonValue \ "category_ids") match {
      case JNothing =>
      case JArray(list) => category_ids = list.map(JInt2Int)
      case _ => throw new IllegalArgumentException
    }
    (jsonValue \ "reputation") match {
      case JNothing => reputation = "Junior"
      case JString(value) => reputation = value.toString
      case _ => throw new IllegalArgumentException
    }
    (jsonValue \ "hourly_price") match {
      case JNothing =>
      case JInt(value) => hourly_price = value.toInt
      case _ => throw new IllegalArgumentException
    }

    this // Return a reference to this object.
  }

  override def validateKeys(keys: Set[String]): Unit = {
    val validKeys: Set[String] = this.toMap.keys.toSet - "id"
    keys == validKeys || keys == validKeys - "reputation" match {
      case false => throw new IllegalArgumentException
      case _ =>
    }
  }

  def validateCategoryIds(validIds: List[Int]): Unit = {
    this.category_ids.toSet.subsetOf(validIds.toSet) match {
      case false => throw new IllegalArgumentException
      case _ =>
    }
  }
  
  override def filter(dict : Map[String, Any]): Boolean = {
    val dictFreelancer : Map[String, Any] = dict - ("id","category_ids")
    var categoryList : List[Int] = dict.get("category_ids").toList.map(a =>
                                                               a.toString.toInt)

    super.filter(dict) && (dictFreelancer.toSet.subsetOf(
                                     (this.toMap - ("id","category_ids")).toSet)
                               
    && categoryList.toSet.subsetOf(this.toMap.get("category_ids").toSet))
  }

  def IncrementTotal_hourly_price(amount: Int): Unit = {
    this.hourly_price += amount
  }
}
