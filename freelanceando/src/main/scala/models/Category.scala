package models

import org.json4s.{DefaultFormats, JValue, JInt, JString}
import org.json4s.DefaultFormats


/* Object companion of class Category */
object Category extends ModelCompanion[Category] {

  implicit lazy val formats = DefaultFormats

  /* This class constructor needs to be overwritten in every subclass of
   * ModelCompanion, because it needs the direct reference to each subclass,
   * in this case, Category.
   */
  /* Este constructor de clase debe sobrescribirse en cada subclase de
   * ModelCompanion, porque necesita la referencia directa a cada subclase,
   * en este caso, Categoría.
   */
  def apply: Category = new Category
}

class Category extends Model[Category] {
  
  // TODO complete here with the methods for your model
  protected[models] var name: String = "DefaultName"
  
  def getName: String = name
  
  override def toMap: Map[String, Any] = super.toMap + ("name" -> name)
    
  override def fromJson(jsonValue: JValue): Category = {
    // TODO Parse jsonValue here and assign the values to
    // the instance attributes.
    (jsonValue \ "id") match {
      case JInt(value) => id = value.toInt
      case _ =>  // Do nothing, things may not have an id
    }
    (jsonValue \ "name") match {
      case JString(value) => name = value.toString
      case _ => // Do nothing, things may not have an name
    }
    
    this // Return a reference to this object.
  }
}
