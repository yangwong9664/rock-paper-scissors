package controllers

import helpers.IntegrationSpecBase
import play.api.libs.ws.DefaultBodyWritables

class MenuControllerITSpec extends IntegrationSpecBase with DefaultBodyWritables {

  "/menu" should {
    "return a 200 for GET given no user session" in {
      val res = buildGetClient("/menu")
      whenReady(res) { response =>
        response.status.intValue() mustBe 200
      }
    }

    "return a 200 for GET given user has a session" in {
      //TODO cookie tests
    }
  }

  "/restart" should {
    "return a 303 for GET" in {
      val res = buildGetClient("/restart")
      whenReady(res) { response =>
        response.status.intValue() mustBe 303
      }
    }
  }

  //TODO POST tests

}
