package models.database

import models._

/* This is a Database abstraction.
 * A Database is a collection of different tables that are loaded into memory.
 */
class Database (val databaseDirname: String) {

  /* Add your own tables here
   * This attribute of the Database keeps track of the existing tables and
   * the constructor (companion object) used to create objects from the
   * information saved in that table.
   */
  /* Agrega tus propias tablas aquí
   * Este atributo de la base de datos realiza un seguimiento de las tablas existentes y
   * el constructor (objeto complementario) utilizado para crear objetos a partir de
   * información guardada en esa tabla.
   */

  // We create a single instance of each table, and then use it when loading
  // and querying instances.
  // Creamos una sola instancia de cada tabla, y luego la usamos al cargar
  // y consultas de instancias.
  val categories = new DatabaseTable[Category](
    s"${databaseDirname}/categories.json")

  val freelancers = new DatabaseTable[Freelancer](
    s"${databaseDirname}/freelancers.json")

  def load: Unit = {
    categories.load(Category)
    freelancers.load(Freelancer)
  }
}
