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
        case None => BadRequest(s"No such freelancer with the id:${id}\n")
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
        val newFreelancer = new Freelancer()
        try {
          val keys = parsedBody.extract[Map[String, Any]].keys.toSet
          val categories = db.categories.all.map(x => x.getId)
          newFreelancer.validateKeys(keys)
          newFreelancer.fromJson(parsedResponse)
          newFreelancer.validateCategoryIds(categories)
          db.freelancers.save(newFreelancer)
          Ok(newFreelancer.getId)
        }
        catch {
          case err: IllegalArgumentException =>
            BadRequest("Invalid parameter\n")
        }
      }
    }
  }

  get("/api/clients") { Ok(db.clients.all.map((x: Client) => x.toMap)) }
  
  post("/api/clients") {
    parsedBody match {
      case JNothing => BadRequest("Bad Json\n")
      case parsedResponse => {
        val newClient = new Client()
        try {
          val keys = parsedBody.extract[Map[String, Any]].keys.toSet
          newClient.validateKeys(keys)
          newClient.fromJson(parsedResponse)
          db.clients.save(newClient)
          Ok(newClient.getId)
        }
        catch {
          case err: IllegalArgumentException =>
            BadRequest("Invalid parameter\n")
        }
      }
    }
  }

  get("/api/clients/:id") {
    val id0: String = params("id")
    try {
      val id: Int = id0.toInt
      db.clients.get(id) match {
        case Some(client) => Ok(client.toMap)
        case None => BadRequest(s"No such client with the id:${id}\n")
      }
    }
    catch {
      case err: java.lang.NumberFormatException =>
        BadRequest(s"The id:${id0} is not an Integer\n")
    }
  }

  get("/api/jobs") { Ok(db.jobs.all.map((x: Job) => x.toMap)) }

/*
  post("api/posts/pay"){
    val id: Int
    val amount : Int
    val clientPay : Client
    val freelancerPay : Frelancer
    val jobPay : Job
    
    try {
      id = params("freelancer_id").toInt
      db.freelancers.get(id) match {
        case Some(freelancer) => freelancerPay = freelancer
        case None => BadRequest(s"No such freelancer with the id:${id}\n")
      }
    }
    catch {
      case err: java.lang.NumberFormatException =>
        BadRequest(s"The id:${id0} is not an Integer\n")
    }
    
    try {
      id = params("job_id").toInt
      db.jobs.get(id) match {
        case Some(job) => jobPay = job
        case None => BadRequest(s"No such job with the id:${id}\n")
      }
    }
    catch {
      case err: java.lang.NumberFormatException =>
        BadRequest(s"The id:${id0} is not an Integer\n")
    }
    
    try {
      amount = params("amount").toInt
      db.clients.get(jobPay._client_id) match {          // Deberia ir getClient
        case Some(client) => clientPay = client
        case None => BadRequest(s"No such client with the id:${id}\n")
      }
    }
    catch {
      case err: java.lang.NumberFormatException =>
        BadRequest(s"The id:${id0} is not an Integer\n")
    }

    clientPay.IncrementTotal_spend(amount)
    freelancerPay.IncrementHourly_price(amount)
    
    
  }
  */

  post("/api/jobs") {
    parsedBody match {
      case JNothing => BadRequest("Bad Json\n")
      case parsedResponse => {
        val newJob = new Job()
        try {
          val keys = parsedBody.extract[Map[String, Any]].keys.toSet
          val category = db.categories.all.map(x => x.getId)
          newJob.validateKeys(keys)
          newJob.fromJson(parsedResponse)
          newJob.validateCategoryId(category)
          db.jobs.save(newJob)
          Ok(newJob.getId)
        }
        catch {
            case err: IllegalArgumentException =>
              BadRequest("Invalid parameter\n")
        }
      } 
    }
  }
  
}

