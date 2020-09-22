package controllers

import helpers.IntegrationSpecBase
import play.api.libs.ws.DefaultBodyWritables

class GameControllerITSpec extends IntegrationSpecBase with DefaultBodyWritables {

  "/play" should {
    "return a 303 for GET given no user session" in {
      val res = buildGetClient("/play")
      whenReady(res) { response =>
        response.status.intValue() mustBe 303
      }
    }

    "return a 200 for GET given user has a session" in {
      //TODO cookie tests
    }
  }

  "/spectate" should {
    "return a 303 for GET given no user session" in {
      val res = buildGetClient("/spectate")
      whenReady(res) { response =>
        response.status.intValue() mustBe 303
      }
    }

    "return a 200 for GET given user has a session" in {
      //TODO cookie tests
    }
  }

  //TODO POST tests

}
