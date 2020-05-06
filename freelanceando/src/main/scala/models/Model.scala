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

  def filter(dict : Map[String, Any]): Boolean = {
    val dictModel : Map[String, Any] = dict.filter(t =>
                                                this.toMap.contains(t._1)).toMap
    dictModel.toSet.subsetOf(this.toMap.toSet)
  }

  def validateKeys(keys: Set[String]): Unit = {
    val validKeys: Set[String] = this.toMap.keys.toSet - "id"
    keys == validKeys match {
      case false => throw new IllegalArgumentException
      case _ =>
    }
  }

}
