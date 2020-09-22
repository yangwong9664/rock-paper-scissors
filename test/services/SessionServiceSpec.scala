package services

import models.{Paper, Rock, SessionModel}
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.mvc.Session
import service.SessionService

class SessionServiceSpec extends PlaySpec with GuiceOneAppPerTest with MockitoSugar {

  val service = SessionService

  "ensureSession" must {

    "create a new session if the user hasn't got one" in {

      val session = Session()
      val sessionResult = Session(Map("session" -> "{}"))

      val result = service.ensureSession(session)

      result mustBe sessionResult
    }

    "retrieve an existing session if the user already has one" in {

      val session = Session(Map("session" -> "{\"gameSpectator\": \"true\"}"))

      val result = service.ensureSession(session)

      result mustBe session
    }
  }

  "sessionToSessionModel" must {

    "convert a session to session model if it exists" in {

      val session = Session(Map("session" -> "{\"gameSpectator\": \"true\"}"))

      val sessionModel = SessionModel(gameSpectator = Some("true"))

      val result = service.sessionToSessionModel(session)

      result mustBe sessionModel
    }

    "return an empty session model if a session does not exist" in {

      val session = Session()

      val sessionModel = SessionModel()

      val result = service.sessionToSessionModel(session)

      result mustBe sessionModel
    }
  }

  "setGameMode" must {

    "not make session changes if the user selects the same game mode" in {

      val sessionModel = SessionModel(gameMode = Some("classic"), gameChoiceOne = Some(Rock.key), gameChoiceTwo = Some(Paper.key))

      val result = service.setGameMode("classic", sessionModel)

      result mustBe sessionModel
    }

    "update session with the new game mode and clear down the last game results if gameMode is changed" in {

      val sessionModel = SessionModel(gameMode = Some("classic"), gameChoiceOne = Some(Rock.key), gameChoiceTwo = Some(Paper.key))
      val sessionModelResult = SessionModel(gameMode = Some("rock"))

      val result = service.setGameMode("rock", sessionModel)

      result mustBe sessionModelResult
    }
  }

  "setSpectatorMode" must {

    "not make session changes if the user selects the same spectator mode" in {

      val sessionModel = SessionModel(gameSpectator = Some("true"), gameChoiceOne = Some(Rock.key), gameChoiceTwo = Some(Paper.key))

      val result = service.setSpectatorMode("true", sessionModel)

      result mustBe sessionModel
    }

    "update session with the new spectator mode and clear down the last game results if spectator mode is changed" in {

      val sessionModel = SessionModel(gameSpectator = Some("true"), gameChoiceOne = Some(Rock.key), gameChoiceTwo = Some(Paper.key))
      val sessionModelResult = SessionModel(gameSpectator = Some("false"))

      val result = service.setSpectatorMode("false", sessionModel)

      result mustBe sessionModelResult
    }
  }

}
