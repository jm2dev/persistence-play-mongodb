package models

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

case class Book(title: String)

object JsonFormats {
  import play.api.libs.json.Json

  implicit val bookFormat = Json.format[Book]
}