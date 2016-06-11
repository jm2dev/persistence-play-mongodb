package controllers

import javax.inject.{Inject, Singleton}

import models.Book
import models.JsonFormats._
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.play.json._
import play.modules.reactivemongo.json.collection._

import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

//import play.api.Logger
import play.api.libs.functional.syntax._
import play.api.libs.json._

// Reactive Mongo imports
import reactivemongo.api.Cursor


// BSON-JSON conversions/collection

@Singleton
class BooksController @Inject() (val reactiveMongoApi: ReactiveMongoApi)
    extends Controller with MongoController with ReactiveMongoComponents {
  def collection: JSONCollection = db.collection[JSONCollection]("boxes")

  def create(title: String) = Action.async {
    val json = Json.obj("title" -> title)

    collection.insert(json).map (lastError => Ok("Mongo LastError: %s".format(lastError)))
  }

  def list() = Action.async {
    val cursor: Cursor[Book] = collection
      .find(Json.obj())
      .cursor[Book]
    val futureBooks: Future[List[Book]] = cursor.collect[List]()
    futureBooks.map {books => Ok(books.toString)}
  }
}
