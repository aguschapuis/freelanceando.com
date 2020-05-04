package models

import org.json4s.{DefaultFormats, JValue, JInt, JString, JNothing}

object Job extends ModelCompanion[Job] {
  
    implicit lazy val format = DefaultFormats

    def apply: Job = new Job
}

class Job extends Model[Job]{

  protected[models] var title: String = "DefaultStr"
  protected[models] var category: Int = 0
  protected[models] var client_id: Int  = 0
  protected[models] var preferred_expertise: String = "DefaultStr"
  protected[models] var preferred_country: String = "DefaultStr"
  protected[models] var hourly_price: Int = 0

  override def toMap: Map[String , Any] = {
    super.toMap + ("title" -> title,
                  "category_id" -> category,
                  "client_id" -> client_id,
                  "preferred_expertise" -> preferred_expertise,
                  "preferred_country" -> preferred_country,
                  "hourly_price"-> hourly_price) 
  }

  override def fromJson (jsonValue: JValue): Job = {
    super.fromJson(jsonValue)
    (jsonValue \ "title") match {
      case JNothing =>
      case JString(value) => title = value.toString
      case _ => throw new IllegalArgumentException
    }
    (jsonValue \ "category_id") match {
      case JNothing =>
      case JInt(value) => category = value.toInt
      case _ => throw new IllegalArgumentException
    }
    (jsonValue \ "client_id") match {
      case JNothing =>
      case JInt(value) => client_id = value.toInt
      case _ => throw new IllegalArgumentException
    }
    (jsonValue \ "preferred_expertise") match {
      case JNothing =>
      case JString(value) => preferred_expertise = value.toString
      case _ => throw new IllegalArgumentException
    }
    (jsonValue \ "preferred_country") match {
      case JNothing =>
      case JString(value) => preferred_country = value.toString
      case _ => throw new IllegalArgumentException
    }
    (jsonValue \ "hourly_price") match {
      case JNothing =>
      case JInt(value) => hourly_price = value.toInt
      case _ => throw new IllegalArgumentException
    }
    this
  }
  
  override def validateNames(names: Set[String]): Unit = {
    val validNames: Set[String] = this.toMap.keys.toSet - "id"
    names.subsetOf(validNames) match {
      case false => throw new IllegalArgumentException
      case _ =>
    }
  }

  def validateCategoryId(validIds: List[Int]): Unit = {
    validIds.contains(this.category) match {
      case false => throw new IllegalArgumentException
      case _ =>
    }
  }

}

