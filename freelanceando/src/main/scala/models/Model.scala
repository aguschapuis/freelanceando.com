package models

import org.json4s.{DefaultFormats, JValue, JInt}
import models.database.DatabaseTable
import scala.collection.mutable.Map._

/* This companion allows us to create models from
 * the DatabaseTable class.
 */
trait ModelCompanion[M <: Model[M]] {

  /* In scala, the apply method is a class constructor. It is used to build
   * new instances of class M with different parameters from the regular
   * constructor.
   */
  def apply: M

}


/* By specifying Model as a trait, we are saying that every class that is a
 * Model, MUST have these attributes and methods.
 * M is just a reference to any class that will (eventually) extend Model.
 * It is necessary, because we need to pass it as a *type parameter*
 * to DatabaseTable.
 * For now, we won't delve into what self is, optionally you can check
 * https://docs.scala-lang.org/tour/self-types.html
 */
trait Model[M <: Model[M]] { self: M =>
  // When no ID is given, we assign the value 0.
  protected[models] var id: Int = 0

  implicit lazy val formats = DefaultFormats

  def getId: Int = id  // By using this function, the id can be viewed by others,
                       // but not modified.

  // Returns a dictionary where keys are the object attributes' names and
  // values are the object attributes' values
  def toMap: Map[String, Any] = Map("id" -> id)

  /* This function fills the object with the attributes in @jsonValue.
   * Every subclass must override this function, because they'll know the
   * types of the new attributes they define.
   */
  def fromJson(jsonValue: JValue): M = {
    (jsonValue \ "id") match {
      case JInt(value) => id = value.toInt
      case _ =>  // Do nothing, things may not have an id
    }
    self
  }


  /* Descripcion:
   *   Controla que el valor de cada uno de los atributos del objeto listados en 
   *   el diccionario, que se toma como argumento de entrada, coincida con cada
   *   valor asociado a cada clave con el mismo nombre en cada atributo. Si el
   *   diccionario contiene claves que no coinciden con algun atributo de la
   *   clase se considera que el objeto no machea con el objeto
   *
   * Parametros:
   *   attributes: Diccionario que contiene nombres de atributos como claves  
   *               y posiblemente claves que no sean nombre de atributos.
   *               
   * Resultados:
   *   True: Si el valor de cada atributo del objeto coincide con el valor 
   *         asociado a cada una de las claves con el mismo nombre y ademas el 
   *         diccionario de entrada no contiene claves que no sean nombres de
   *         atributos de la Clase. 
   *   False: Si el diccionario de entrada tiene claves que no son nombres de
   *          atributos de la Clase, o si el valor de algun atributo del objeto
   *          no coincide con el valor asociado a la clave con el mismo nombre. 
   */

  def matchWithFilters(attributes: Map[String, Any]): Boolean = {
    val validFilterKeys: Set[String] = Set("preferred_country",
                                          "category_id",
                                          "preferred_reputation",
                                          "hourly_price")
    attributes.keys.toSet.subsetOf(validFilterKeys) match {
      case true => attributes.toSet.subsetOf(this.toMap.toSet)
      case _ => false
    }
  }

  /* Descripcion:
   *   Controla que todos los elemento del conjunto de entrada coincidan con
   *   al menos uno de los atributos de la clase y que todos los atributos de la 
   *   Clase menos el atributo id esten representados en dicho conjunto, si esto
   *   no es asi se levanta una excepcion en caso contrario termina con
   *   normalidad.
   *
   * Parametros:
   *   keys: Conjunto que contiene elementos que pueden coincidir con nombres de
   *         atributos o no.
   *   
   * Resultados:
   *   Ninguno
   */

  def validateKeys(keys: Set[String]): Unit = {
    val validKeys: Set[String] = this.toMap.keys.toSet - "id"
    keys == validKeys match {
      case false => throw new IllegalArgumentException
      case _ =>
    }
  }

}
