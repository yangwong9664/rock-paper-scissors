package controllers

import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test.Helpers._
import play.api.test._

class GameControllerSpec extends PlaySpec with GuiceOneAppPerTest with MockitoSugar {

  "GameController GET" must {

    "render the game page from a new instance of controller" in {
      val controller = new GameController(stubControllerComponents())
      val home = controller.game(None).apply(FakeRequest(GET, "/").withSession(("gameMode","classic")))

      status(home) mustBe OK
    }
 }

}
