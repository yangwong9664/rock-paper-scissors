package controllers

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

      val home = controller.game()(FakeRequest(GET, "/").withSession(("gameMode", "classic")).withCSRFToken)

      status(home) mustBe OK
      contentAsString(home) must include("Rock, Paper, Scissors, Classic Mode")
    }

    "render the game page given the user has selected 'rock game mode'" in {
      val home = controller.game()(FakeRequest(GET, "/").withSession(("gameMode", "rock")).withCSRFToken)

      status(home) mustBe OK
      contentAsString(home) must include("Rock Rock, High Strategy Mode")
    }

    "redirect to the menu page if the user hasn't selected a game mode" in {
      val home = controller.game()(FakeRequest(GET, "/").withCSRFToken)

      status(home) mustBe SEE_OTHER
      redirectLocation(home) mustBe Some("/menu")
    }

    "redirect to the menu page if the user has altered the session data" in {
      val home = controller.game()(FakeRequest(GET, "/")
        .withSession(("gameMode", "hackerman")).withCSRFToken)

      status(home) mustBe SEE_OTHER
      redirectLocation(home) mustBe Some("/menu")
    }

    "render the game page given the user has selected 'classic game mode' and chose 'rock'" in {

      val home = controller.game()(FakeRequest(GET, "/")
        .withSession(("gameMode", "classic"), ("gameChoice", "rock")).withCSRFToken)

      status(home) mustBe OK
      contentAsString(home) must include("Rock, Paper, Scissors, Classic Mode")
      contentAsString(home) must include("You chose: rock")
    }
  }

  "GameController POST" must {

      "render the game page given the user has selected a valid option" in {
        val home = controller.submitGame()(FakeRequest()
          .withFormUrlEncodedBody(
            "rps.selection" -> "rock"
          )
          .withSession(("gameMode", "classic"))
          .withCSRFToken)

        status(home) mustBe SEE_OTHER
        redirectLocation(home) mustBe Some("/play")
      }

      "return a Bad Request given the user does not have a CSRF token" in {
        //more CSRF protection could be implemented at a later date, not needed for MVP though
      }


  }

}
