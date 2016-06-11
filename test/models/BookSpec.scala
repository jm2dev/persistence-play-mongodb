package models

import common.UnitSpec
import play.api.libs.json._

class BookSpec extends UnitSpec {
  val book = Book("the art of shaving")

  "A book" should "have a json representation" in {
    val expected = Json.obj("title" -> JsString("the art of shaving"))
    val jsonBook = Json.toJson(book)
    assert(jsonBook === expected)
  }

  "A book" should "have a minified json string" in {
    val expected = """{"title":"the art of shaving"}"""
    val jsonBook = Json.toJson(book)
    assert(Json.stringify(jsonBook) === expected)
  }
}

