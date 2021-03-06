package app

import org.json4s.{DefaultFormats, Formats, JValue, JNothing, JInt}

import org.scalatra._
import org.scalatra.json._
import models._
import models.database.Database
import org.json4s.JsonAST.JString
import scala.collection.mutable


class FreelanceandoServlet(db : Database) extends ScalatraServlet 
                                                      with JacksonJsonSupport {

  // Before every action runs, set the content type to be in JSON format.
  protected implicit val jsonFormats: Formats = DefaultFormats
  before() {
    contentType = formats("json")
  }
  
  /*--------------END POINTS CATEGORIES-------------*/

  get("/api/categories") { Ok(db.categories.all.map((x: Category) => x.toMap)) }
  

  /*--------------END POINTS FREELANCERS-------------*/
  
  get("/api/freelancers") {
      var attributes = Map[String, Any]()
      var listaux = mutable.ListBuffer[(String, Any)]()
      params.get("country_code") match {
        case Some(value) => listaux += "country_code" -> value
        case None => 
      }
      (params.get("reputation")) match {
        case Some(value) => listaux += "reputation" -> value
        case None =>  
      }
      (params.get("category_id")) match {
        case Some(value) => listaux += "category_id" -> value.toInt
        case None =>  
      }
      (params.get("hourly_price")) match {
        case Some(value) => listaux += "hourly_price" -> value.toInt
        case None =>  
      }
      attributes = listaux.toMap
      Ok(db.freelancers.filter(attributes).map(x => x.toMap))
  }

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


  /*--------------END POINTS CLIENTS-------------*/
  
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

  
  /*--------------END POINTS JOBS-------------*/
  
  get("/api/jobs") { Ok(db.jobs.all.map((x: Job) => x.toMap)) }
  
  get("/api/posts"){
    var attributes = Map[String, Any]()
    var listaux = mutable.ListBuffer[(String, Any)]()
    params.get("title") match {
      case Some(value) => listaux += "title" -> value
      case None => 
    }
    (params.get("client_id")) match {
      case Some(value) => listaux += "client_id" -> value.toInt
      case None =>  
    }
    (params.get("category_id")) match {
      case Some(value) => listaux += "category_id" -> value.toInt
      case None =>  
    }
    (params.get("preferred_expertise")) match {
      case Some(value) => listaux += "preferred_expertise" -> value
      case None =>  
    }
    (params.get("preferred_country")) match {
      case Some(value) => listaux += "preferred_country" -> value
      case None =>  
    }
    (params.get("hourly_price")) match {
      case Some(value) => listaux += "hourly_price" -> value.toInt
      case None =>  
    }
    attributes = listaux.toMap
    Ok(db.jobs.filter(attributes).map((x: Job) => x.toMap))    
  }

  
  post("/api/jobs") {
    parsedBody match {
      case JNothing => BadRequest("Bad Json\n")
      case parsedResponse => {
        val newJob = new Job()
        try {
          val keys = parsedBody.extract[Map[String, Any]].keys.toSet
          val category = db.categories.all.map(x => x.getId)
          val clients = db.clients.all.map(x => x.getId)
          newJob.validateKeys(keys)
          newJob.fromJson(parsedResponse)
          newJob.validateCategoryId(category)
          newJob.validateClientId(clients)
          db.jobs.save(newJob)
          val clientId: Int = newJob.getClient_id
          db.clients.get(clientId) match {
            case None => throw new IllegalArgumentException
            case Some(client) =>
              client.add_job(newJob.getClient_id) 
          }
          db.clients.write
          Ok(newJob.getId)
        }
        catch {
          case err: IllegalArgumentException =>
          BadRequest("Invalid parameter\n")
        }
      } 
    }
  }
  

  /*--------------END POINTS PAYS-------------*/

  post("/api/posts/pay"){
    parsedBody match {
      case JNothing => BadRequest("Bad Json\n")
      case parsedResponse => {
        try {
          val freelancertId = (parsedResponse \ "freelancert_id").extract[Int]
          val jobId: Int = (parsedResponse \ "job_id").extract[Int]
          val amount: Int = (parsedResponse \ "amount").extract[Int]
          
          db.freelancers.get(freelancertId) match {
            case None => throw new IllegalArgumentException
            case Some(freelancer) => {
              db.jobs.get(jobId) match {
                case None => throw new IllegalArgumentException
                case Some(job) => {
                  val clientId: Int = job.getClient_id
                  db.clients.get(clientId) match {
                    case None => throw new IllegalArgumentException
                    case Some(client) => {
                      // Efectuamos el pago:
                      freelancer.IncrementTotal_earnings(amount)
                      client.IncrementTotal_spend(amount)
                      db.freelancers.write
                      db.clients.write
                      Ok()
                    }
                  }
                }
              }
            }
          }
        }
        catch {
          case err1: org.json4s.MappingException =>
          BadRequest("Invalid parameter\n")
          case err2: IllegalArgumentException =>
          NotFound("Invalid id\n")
        }
      }
    }
  }

}
