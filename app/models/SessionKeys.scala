package models

import scala.language.implicitConversions

sealed trait SessionKeys {
  val key: String
}

case object SessionCookieName extends SessionKeys {
  override val key: String = "session"
}
case object GameMode extends SessionKeys {
  override val key: String = "gameMode"
}
case object GameSpectator extends SessionKeys {
  override val key: String = "gameSpectator"
}
case object GameChoiceOne extends SessionKeys {
  override val key: String = "gameChoiceOne"
}
case object GameChoiceTwo extends SessionKeys {
  override val key: String = "gameChoiceTwo"
}

object SessionKeys {

  implicit def stringToGameMode(string: String): SessionKeys = {
    string match {
      case GameMode.key => GameMode
      case GameSpectator.key => GameSpectator
      case GameChoiceOne.key => GameChoiceOne
      case GameChoiceTwo.key => GameChoiceTwo
    }
  }

}
