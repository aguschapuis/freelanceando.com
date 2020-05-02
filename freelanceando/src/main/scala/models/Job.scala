package models

import org.json4s.{DefaultFormats, JValue, JInt, JString, JArray}

object Job extends ModelCompanion[Job] {
  
    implicit lazy val format = DefaultFormats

    def apply: Job = new Job
}

// {id: int, title: str, category_id: int, client_id: int, preferred_expertise: str, preferred_country: str, hourly_price: int}

class Job(title: String, category_id: Int, client_id: Int, preferred_expertise: String, preferred_country: String,
          hourly_price: Int) extends Model[Job]{

  def this() = this(title = "", category_id = 0, client_id = 0, preferred_expertise = "", preferred_country = "", hourly_price = 0)

  protected[models] var _title: String = title
  protected[models] var _category: Int = category_id
  protected[models] var _client_id: Int  = client_id
  protected[models] var _preferred_expertise: String = preferred_expertise
  protected[models] var _preferred_country: String = preferred_country
  protected[models] var _hourly_price: Int = hourly_price

  override def toMap: Map[String , Any] = {
    super.toMap + ("title" -> _title,
                  "category_id" -> _category,
                  "client_id" -> _client_id,
                  "preferred_expertise" -> _preferred_expertise,
                  "preferred_country" -> _preferred_country,
                  "hourly_price"-> _hourly_price) 
  }

  override def fromJson (jsonValue: JValue): Job = {
    super.fromJson(jsonValue)
    (jsonValue \ "title") match {
      case JString(value) => _title = value.toString
      case _ => 
    }
    (jsonValue \ "category_id") match {
      case JInt(value) => _category = value.toInt
      case _ => 
    }
    (jsonValue \ "client_id") match {
      case JInt(value) => _client_id = value.toInt
      case _ => 
    }
    (jsonValue \ "preferred_expertise") match {
      case JString(value) => _preferred_expertise = value.toString
      case _ => 
    }
    (jsonValue \ "preferred_country") match {
      case JString(value) => _preferred_country = value.toString
      case _ => 
    }
    (jsonValue \ "hourly_price") match {
      case JInt(value) => _hourly_price = value.toInt
      case _ => 
    }
    this
  }
    

}

