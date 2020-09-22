package helpers

import akka.actor.ActorSystem
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.core.WireMockConfiguration._
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import models.SessionModel
import org.scalatest.concurrent.{Eventually, IntegrationPatience}
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.libs.json.Json
import play.api.libs.ws.{DefaultWSCookie, WSClient, WSResponse}
import play.shaded.ahc.io.netty.handler.codec.http.cookie.DefaultCookie

import scala.concurrent.Future

object WiremockHelper extends Eventually with IntegrationPatience {

  val wiremockPort = 1111
  val wiremockHost = "localhost"
  val wiremockURL = s"http://$wiremockHost:$wiremockPort"

  def stubGet(url: String, status: Integer, body: String): StubMapping = {
    stubFor(get(urlMatching(url))
      .willReturn(
        aResponse().
          withStatus(status).
          withBody(body)
      )
    )
  }

  def stubPost(url: String, status: Integer, responseBody: String): StubMapping =
    stubFor(post(urlPathMatching(url))
      .willReturn(
        aResponse().
          withStatus(status).
          withBody(responseBody)
      )
    )
}

trait WiremockHelper {
  self: GuiceOneServerPerSuite =>

  import WiremockHelper._
  implicit val system = ActorSystem("my-system")
  lazy val ws = app.injector.instanceOf[WSClient]

  lazy val wmConfig = wireMockConfig().port(wiremockPort)
  lazy val wireMockServer = new WireMockServer(wmConfig)

  def startWiremock(): Unit = {
    wireMockServer.start()
    WireMock.configureFor(wiremockHost, wiremockPort)
  }

  def stopWiremock(): Unit = wireMockServer.stop()

  def resetWiremock(): Unit = WireMock.reset()

  lazy val csrfCookie = DefaultWSCookie("CSRF-Token","cookie.value")
  lazy val playLangCookie = DefaultWSCookie("PLAY_LANG", "en-GB")

  //TODO refactor these methods
  def buildGetClient(path: String, sessionModel: Option[SessionModel] = None, followRedirects: Boolean = false): Future[WSResponse] = {
    if(sessionModel.isDefined){
      ws.url(s"http://localhost:$port$path")
        .withFollowRedirects(followRedirects)
        .withCookies(csrfCookie, playLangCookie) //TODO get cookie tests working with session
        .get()
    }
    ws.url(s"http://localhost:$port$path")
      .withFollowRedirects(followRedirects)
        .withCookies(csrfCookie, playLangCookie)
      .get()
  }

  def buildPostClient(path: String, body: String, sessionModel: Option[SessionModel] = None, followRedirects: Boolean = false): Future[WSResponse] = {
    if(sessionModel.isDefined){
      ws.url(s"http://localhost:$port$path")
        .withFollowRedirects(followRedirects)
        .withCookies(csrfCookie, playLangCookie) //TODO get cookie tests working with session
        .post(body)
    }
    ws.url(s"http://localhost:$port$path")
      .withFollowRedirects(followRedirects)
      .withCookies(csrfCookie, playLangCookie)
      .post(body)
  }
}
