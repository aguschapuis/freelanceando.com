package app

import org.json4s.{DefaultFormats, Formats, JValue, JNothing}

import org.scalatra._
import org.scalatra.json._
import models._
import models.database.Database


class FreelanceandoServlet(db : Database) extends ScalatraServlet with JacksonJsonSupport {

  // Before every action runs, set the content type to be in JSON format.
  protected implicit val jsonFormats: Formats = DefaultFormats
  before() {
    contentType = formats("json")
  }
  
  // Here you have to complete all the API endopoints.
  get("/api/categories") { Ok(db.categories.all.map((x: Category) => x.toMap)) }
  
  get("/api/freelancers") { Ok(db.freelancers.all.map((x: Freelancer) => x.toMap)) }

  get("/api/freelancers/:id") {
    val id0: String = params("id")
    try { 
      val id: Int = id0.toInt
      db.freelancers.get(id) match {
        case Some(freelancer) => Ok(freelancer.toMap)
        case None => BadRequest(s"No such category with the id:${id}\n")
      }
    }
    catch {
      case err: java.lang.NumberFormatException =>
        BadRequest(s"The id:${id0} is not an Integer\n")
    }
  }
  post("/api/freelancers") {
   parsedBody match {
     case JNothing => BadRequest("Bad Json\n")
     case parsedResponse => {
       val freelancer = new Freelancer("Juan", "Argetina", List(1,3), "Junior", 20)
       // Do things to create client here
       Ok(freelancer.getId)
     }
   }
 }

 get("/api/jobs") { Ok(db.jobs.all.map((x: Job) => x.toMap)) }

}

