package models

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

object Book {

  implicit val bookWrites = new Writes[Book] {
    def writes(book: Book) = Json.obj(
      "title" -> book.title
    )
  }
  implicit val bookReads = Json.reads[Book]
}

case class Book(title: String)
