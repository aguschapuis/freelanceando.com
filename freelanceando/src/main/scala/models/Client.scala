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
    super.toMap + ("username" -> username,
                  "country_code"-> country_code,
                  "total_spend" -> total_spend)
  }


  /* Descripcion:
   *   Metodo sobreescrito que controla que todos los elemento del conjunto de
   *   entrada coincidan con al menos uno de los atributos de la clase y que
   *   todos los atributos de la Clase menos el atributo id y total_spend esten
   *   representados en dicho conjunto, si esto no es asi se levanta una
   *   excepcion en caso contrario termina con normalidad.
   *
   * Parametros:
   *   keys: Conjunto que contiene elementos que pueden coincidir con nombres de
   *         atributos o no.
   * 
   * Resultados:
   *   Ninguno
   */


  override def validateKeys(keys: Set[String]): Unit = {
    val validKeys: Set[String] = this.toMap.keys.toSet - ("id", "total_spend")
    keys == validKeys match {
      case false => throw new IllegalArgumentException
      case _ =>
    }
  }


  /* Descripcion:
   *   Incrementa el valor del atributo total_spend de acuerdo al valor del   
   *   argumento que se ingresa como del parametro de entrada.
   *
   * Parametros:
   *   amount: Cantidad de dinero gastado por Cliente en el trabajo encargado al
   *           al Freelancer.
   * 
   * Resultados:
   *   Ninguno
   */

  def IncrementTotal_spend(amount : Int) : Unit = {
    this.total_spend += amount
  }
 
}
