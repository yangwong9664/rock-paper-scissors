package controllers

import helpers.IntegrationSpecBase
import play.api.libs.ws.DefaultBodyWritables

class GameControllerITSpec extends IntegrationSpecBase with DefaultBodyWritables {

  "home" should {
    "return a 200 for GET" in {
      val res = buildGetClient("/play")
      whenReady(res) { response =>
        response.status.intValue() mustBe 303
      }
    }
  }

}
