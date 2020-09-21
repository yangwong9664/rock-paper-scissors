package controllers

import models.SessionModel
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test.CSRFTokenHelper._
import play.api.test.Helpers._
import play.api.test._

class GameControllerSpec extends PlaySpec with GuiceOneAppPerTest with MockitoSugar {

  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global
  val controller = new GameController(stubControllerComponents())

  "GameController GET" must {

    "render the game page given the user has selected 'classic game mode'" in {

      val home = controller.game()(FakeRequest(GET, "/").withSession(("session", "{\"gameMode\": \"classic\"}")).withCSRFToken)

      status(home) mustBe OK
      contentAsString(home) must include("Rock, Paper, Scissors, Classic Mode")
    }

    "render the game page given the user has selected 'rock game mode'" in {
      val home = controller.game()(FakeRequest(GET, "/").withSession(("session", "{\"gameMode\": \"rock\"}")).withCSRFToken)

      status(home) mustBe OK
      contentAsString(home) must include("Rock Rock, High Strategy Mode")
    }

    "redirect to the menu page if the user hasn't selected a game mode" in {
      val home = controller.game()(FakeRequest(GET, "/").withCSRFToken)

      status(home) mustBe SEE_OTHER
      redirectLocation(home) mustBe Some("/menu")
    }

    "redirect to the menu page if the user hasn't got a game mode" in {
      val home = controller.game()(FakeRequest(GET, "/").withSession(("session", "{}")).withCSRFToken)

      status(home) mustBe SEE_OTHER
      redirectLocation(home) mustBe Some("/menu")
    }

    "render the game page given the user has selected 'classic game mode' and chose 'rock', and AI chose paper" in {

      val home = controller.game()(FakeRequest(GET, "/")
        .withSession(("session", "{\"gameMode\": \"classic\", \"gameChoiceOne\": \"rock\", \"gameChoiceTwo\": \"paper\"}")).withCSRFToken)

      status(home) mustBe OK
      contentAsString(home) must include("Rock, Paper, Scissors, Classic Mode")
      contentAsString(home) must include("You chose: rock")
      contentAsString(home) must include("AI chose: paper")
    }
  }

  "GameController POST" must {

      "redirect to the game page given the user has selected a valid option" in {
        val home = controller.submitGame()(FakeRequest()
          .withFormUrlEncodedBody(
            "rps.selection" -> "rock"
          )
          .withSession(("session", "{\"gameMode\": \"classic\"}"))
          .withCSRFToken)

        status(home) mustBe SEE_OTHER
        redirectLocation(home) mustBe Some("/play")
      }

    "return a bad request given an invalid form choice" in {
      val home = controller.submitGame()(FakeRequest()
        .withFormUrlEncodedBody(
          "rps.selection" -> "hacker"
        )
        .withSession(("session", "{\"gameMode\": \"classic\"}"))
        .withCSRFToken)

      status(home) mustBe BAD_REQUEST
    }

    "return a bad request given an invalid form choice for a different game mode" in {
      val home = controller.submitGame()(FakeRequest()
        .withFormUrlEncodedBody(
          "rps.selection" -> "scissors"
        )
        .withSession(("session", "{\"gameMode\": \"rock\"}"))
        .withCSRFToken)

      status(home) mustBe BAD_REQUEST
    }

    "return a bad request given an empty value" in {
      val home = controller.submitGame()(FakeRequest()
        .withFormUrlEncodedBody(
          "rps.selection" -> ""
        )
        .withSession(("session", "{\"gameMode\": \"classic\"}"))
        .withCSRFToken)

      status(home) mustBe BAD_REQUEST
    }

    "return a bad request given no form value" in {
      val home = controller.submitGame()(FakeRequest()
        .withSession(("session", "{\"gameMode\": \"classic\"}"))
        .withCSRFToken)

      status(home) mustBe BAD_REQUEST
    }
  }

  "GameController GET spectatorMode" must {

    "redirect to the game page given the user has selected 'classic spectator game mode'" in {

      val home = controller.spectate()(FakeRequest(GET, "/")
        .withSession(("session", "{\"gameMode\": \"classic\",\"gameSpectator\": \"true\"}")).withCSRFToken)

      status(home) mustBe SEE_OTHER
      redirectLocation(home) mustBe Some("/play")
    }

    "redirect if the user doesn't have a game mode" in {

      val home = controller.spectate()(FakeRequest(GET, "/")
        .withSession(("session", "{\"gameSpectator\": \"true\"}")).withCSRFToken)

      status(home) mustBe SEE_OTHER
      redirectLocation(home) mustBe Some("/menu")
    }

    "redirect if the user doesn't have spectator mode enables" in {

      val home = controller.spectate()(FakeRequest(GET, "/")
        .withSession(("session", "{\"gameMode\": \"classic\"}")).withCSRFToken)

      status(home) mustBe SEE_OTHER
      redirectLocation(home) mustBe Some("/menu")
    }
  }

}
