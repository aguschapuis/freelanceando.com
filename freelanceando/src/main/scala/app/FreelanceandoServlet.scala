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

}

