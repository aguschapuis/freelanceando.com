package app

import org.json4s.{DefaultFormats, Formats, JValue, JNothing, JInt}

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

  post("/api/posts/pay"){
    val jsonParsed:JValue = parse(request.body)

    try {
      val freelancertId: Int = (jsonParsed \ "freelancert_id").extract[Int]
      val jobId: Int = (jsonParsed \ "job_id").extract[Int]
      val amount: Int = (jsonParsed \ "amount").extract[Int]
      
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
                  freelancer.IncrementTotal_hourly_price(amount)
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
        BadRequest("Invalid parameter\n")
    }
  }

  get("/api/posts/"){
    val jsonParsed:JValue = parse(request.body)
    val atributes = jsonParsed.extract[Map[String, Any]]
    
    Ok(db.jobs.filter(atributes).map((x: Job) => x.toMap))
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
          Ok(newJob.getId)
        }
        catch {
            case err: IllegalArgumentException =>
              BadRequest("Invalid parameter\n")
        }
      } 
    }
  }

  get("api/freelancers"){
    parsedBody match {
      case JNothing => BadRequest("Bad Json\n")
      case parsedResponse => {
        try {
          val list : List[Freelancer]
          val dict : Map[String, Any]= parsedBody.extract[Map[String, Any]]
          val keys = dict.keys.toSet
          //Frelacer.validateKeys(keys)
          list = db.freelancers.filter(dict)
          if (list.isEmpty) Ok(db.freelancers.all) else Ok(list)
        }
        catch {
            case err: IllegalArgumentException =>
              BadRequest("Invalid parameter\n")
        }
       }
      }
   }

}

