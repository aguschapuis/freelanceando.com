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

  def getClient_id: Int = client_id

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

  override def matchWithFilters(attributes: Map[String, Any]): Boolean = {
    val validFilterKeys: Set[String] = Set("title",
                                          "client_id",
                                          "category_id",
                                          "preferred_expertise",
                                          "preferred_country",
                                          "hourly_price")
    attributes.keys.toSet.subsetOf(validFilterKeys) match {
      case true => {
        attributes.contains("category_id") match {
          case true => {
            var id: Int = attributes("category_id").toString.toInt
            this.category == id &&
              (attributes - "category_id").toSet.subsetOf(this.toMap.toSet)
          }
          case false => attributes.toSet.subsetOf(this.toMap.toSet)
        }
      }
      case _ => false
    }
  }

  /* Descripcion:
   *  Controla que la categoria del trabajo posteado exista entre las 
   *  categorias ya definidas, si esto no es asi se levanta una excepcion
   *  en caso contrario termina con normalidad.
   * 
   * Parametros:
   *  validIds: Lista de enteros que contiene las categorias existentes
   * 
   * Resultado:
   *  Ninguno
  */

  def validateCategoryId(validIds: List[Int]): Unit = {
    validIds.contains(this.category) match {
      case false => throw new IllegalArgumentException
      case _ =>
    }
  }

  /* Descripcion:
   *  Controla que el cliente que ofrece el trabajo exista entre los clientes 
   *  ya registrados en la pagina, si esto no es asi se levanta una excepcion
   *  en caso contrario termina con normalidad.
   * 
   * Parametros:
   *  validIds: Lista de enteros que contiene los clientes existentes
   * 
   * Resultado:
   *  Ninguno
  */

  def validateClientId(validIds: List[Int]): Unit = {
    validIds.contains(this.client_id) match {
      case false => throw new IllegalArgumentException
      case _ =>
    }
  }

}

