package models

import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import SessionKeys._

class SessionKeysSpec extends PlaySpec with GuiceOneAppPerTest with MockitoSugar {

  "SessionKeys" must {

    "return the key for SessionCookieName" in {
      SessionCookieName.key mustBe "session"
    }

    "return the key for GameMode" in {
      GameMode.key mustBe "gameMode"
    }
    "return the key for GameSpectator" in {
      GameSpectator.key mustBe "gameSpectator"
    }

    "return the key for GameChoiceOne" in {
      GameChoiceOne.key mustBe "gameChoiceOne"
    }

    "return the key for GameChoiceTwo" in {
      GameChoiceTwo.key mustBe "gameChoiceTwo"
    }
  }

  "stringToGameMode" must {

    "turn a GameMode string to GameMode" in {
      stringToSessionKey("gameMode") mustBe GameMode
    }

    "turn a GameSpectator string to GameSpectator" in {
      stringToSessionKey("gameSpectator") mustBe GameSpectator
    }

    "turn a GameChoiceOne string to GameChoiceOne" in {
      stringToSessionKey("gameChoiceOne") mustBe GameChoiceOne
    }

    "turn a GameChoiceTwo string to GameChoiceTwo" in {
      stringToSessionKey("gameChoiceTwo") mustBe GameChoiceTwo
    }
  }
}
