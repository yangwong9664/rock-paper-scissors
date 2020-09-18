package helpers

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, HttpResponse}
import akka.stream.ActorMaterializer
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.core.WireMockConfiguration._
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import org.scalatest.concurrent.{Eventually, IntegrationPatience}
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.libs.ws.{DefaultWSCookie, WSClient, WSResponse}
import play.filters.csrf.{CSRFConfigProvider, CSRFFilter}

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

  def buildGetClient(path: String): Future[HttpResponse] = Http().singleRequest(
    HttpRequest(
      HttpMethods.GET,
      uri = s"http://localhost:$port$path"
    )
  )

  def buildPostClient(path: String, body: String): Future[WSResponse] = {
    ws.url(s"http://localhost:$port$path")
      .addCookies(DefaultWSCookie("CSRF-Token","cookie.value"),
        DefaultWSCookie("PLAY_LANG", "en-GB"))
      .post(body)
  }

}
