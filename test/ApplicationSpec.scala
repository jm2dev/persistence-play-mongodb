import models.Book
import models.JsonFormats._
import org.scalatestplus.play._
import play.api.libs.json.Json
import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class ApplicationSpec extends PlaySpec with OneAppPerTest {
  "Routes" should {
    "send 404 on a bad request" in  {
      route(app, FakeRequest(GET, "/boum")).map(status(_)) mustBe Some(NOT_FOUND)
    }
  }

  "HomeController" should {
    "render the index page" in {
      val home = route(app, FakeRequest(GET, "/")).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Your new application is ready.")
    }
  }

  "CountController" should {
    "return an increasing count" in {
      contentAsString(route(app, FakeRequest(GET, "/count")).get) mustBe "0"
      contentAsString(route(app, FakeRequest(GET, "/count")).get) mustBe "1"
      contentAsString(route(app, FakeRequest(GET, "/count")).get) mustBe "2"
    }
  }

  "BooksController" should {
    "create default book" in {
      val book = route(app, FakeRequest(GET, "/book/create")).get

      status(book) mustBe CREATED
    }

    "persist json book" in {
      val book = Json.toJson(Book("el ingenioso hidalgo don quijote de la mancha"))
      val newBook = route(app, FakeRequest(POST, "/book").withJsonBody(book))

      status(newBook.get) mustBe CREATED
    }

    "list all books" in {
      val books = route(app, FakeRequest(GET, "/books")).get

      status(books) mustBe OK
    }
  }
}
