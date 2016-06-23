package controllers

// BSON-JSON conversions/collection
import reactivemongo.play.json.collection.JSONCollection
import reactivemongo.play.json._
import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import akka.stream.Materializer
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.api.Cursor

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

@Singleton
class BooksController @Inject() (implicit system: ActorSystem, materializer: Materializer, val reactiveMongoApi: ReactiveMongoApi)
    extends Controller with MongoController with ReactiveMongoComponents {
  def collection: JSONCollection = Await.result(reactiveMongoApi.database.map(_.collection[JSONCollection]("books")), Duration.Inf)

  import models.JsonFormats._
  import models._

  def create = Action.async {
    val book = Book("bello gallico")
    collection.insert(book).map(_ => Created)
  }

  def persist = Action.async(parse.json) { implicit request =>
    request.body.validate[Book].map { book =>
      Logger.info(book.toString)
      collection.insert(book).map { lastError =>
        Logger.debug(s"Successfully inserted with LastError: $lastError")
        Created
      }
    }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def list() = Action.async { implicit request =>
    val cursor: Cursor[Book] = collection
      .find(Json.obj())
      .sort(Json.obj("created" -> -1))
      .cursor[Book]
    val futureBooks: Future[List[Book]] = cursor.collect[List]()
    futureBooks.map {books => Ok(books.toString)}
  }
}
