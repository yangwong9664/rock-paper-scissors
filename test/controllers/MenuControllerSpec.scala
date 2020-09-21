package controllers

import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.mvc.Session
import play.api.test.CSRFTokenHelper._
import play.api.test.Helpers._
import play.api.test._

class MenuControllerSpec extends PlaySpec with GuiceOneAppPerTest with MockitoSugar {

  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global
  val controller = new MenuController(stubControllerComponents())

  "MenuController GET" must {

    "render the menu page given the user has no session" in {

      val home = controller.menu()(FakeRequest(GET, "/").withCSRFToken)

      status(home) mustBe OK
      contentAsString(home) must include("Select a game mode:")
    }

    "render the menu page given the user has an empty session" in {
      val home = controller.menu()(FakeRequest(GET, "/").withSession(("session", "{}")).withCSRFToken)

      status(home) mustBe OK
      contentAsString(home) must include("Select a game mode:")
    }

    "render the menu page given the user has already selected a game mode" in {
      val home = controller.menu()(FakeRequest(GET, "/").withSession(("session", "{\"gameMode\": \"classic\"}")).withCSRFToken)

      status(home) mustBe OK
      contentAsString(home) must include("Select a game mode:")
    }
  }

  "GameController POST" must {

      "redirect to the game page given the user has selected a valid option" in {
        val home = controller.submitMenu()(FakeRequest()
          .withFormUrlEncodedBody(
            "game.mode.selection" -> "classic"
          )
          .withCSRFToken)

        status(home) mustBe SEE_OTHER
        redirectLocation(home) mustBe Some("/play")
      }

    "return a bad request given an invalid form choice" in {
      val home = controller.submitMenu()(FakeRequest()
        .withFormUrlEncodedBody(
          "game.mode.selection" -> "hacker"
        )
        .withCSRFToken)

      status(home) mustBe BAD_REQUEST
    }

    "return a bad request given an empty value" in {
      val home = controller.submitMenu()(FakeRequest()
        .withFormUrlEncodedBody(
          "game.mode.selection" -> ""
        )
        .withCSRFToken)

      status(home) mustBe BAD_REQUEST
    }

    "return a bad request given no form value" in {
      val home = controller.submitMenu()(FakeRequest()
        .withCSRFToken)

      status(home) mustBe BAD_REQUEST
    }
  }

  "GameController GET Restart" must {

    "redirect to the menu page and clear all session data" in {

      val home = controller.restart()(FakeRequest(GET, "/")
        .withSession(("session", "{\"gameMode\": \"classic\",\"gameSpectator\": \"true\"}")).withCSRFToken)

      status(home) mustBe SEE_OTHER
      redirectLocation(home) mustBe Some("/menu")
      session(home) mustBe Session.emptyCookie
    }
  }

}
