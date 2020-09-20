package models

import play.api.mvc.Session


sealed trait RPS {
  val key: String
}

case object Rock extends RPS {
  override val key: String = "rock"
}
case object Paper extends RPS {
  override val key: String = "paper"
}
case object Scissors extends RPS {
  override val key: String = "scissors"
}

object RPS {

  lazy val classicGameMode: GameMode = GameMode("Rock, Paper, Scissors, Classic Mode", "classic",Seq(Rock, Paper, Scissors))

  lazy val rockGameMode: GameMode = GameMode("Rock Rock, High Strategy Mode", "rock", Seq(Rock, Rock))

  //Expansions can be added here

  lazy val gameModes: Seq[GameMode] = Seq(classicGameMode, rockGameMode)

  //TODO refactor these

  def getGameMode(implicit session: Session): Option[GameMode] = {
    session.get("gameMode") match {
      case Some(gameMode) => gameModes.find(_.key == gameMode)
      case None => None
    }
  }

  def setGameMode(choice: String)(implicit session: Session): Option[Session] = {

    (ensureValidGameMode(choice), getGameMode) match {
      case (true, Some(currentMode)) if currentMode.key == choice => Some(session)
      case (true, Some(_)) => Some(clearRPSChoice. +("gameMode", choice))
      case (true, None) => Some(clearRPSChoice. +("gameMode", choice))
      case _ => None
    }
    //this allows a user to have their game saved, unless they switch game modes
  }

  def setRPSChoice(choice: String, gameMode: GameMode)(implicit session: Session): Option[Session] = {
    if(ensureValidRPSSelection(choice, gameMode)){
      Some(session. +("gameChoice",choice))
    } else {
      None
    }
  }

  def getRPSChoice(implicit session: Session): Option[String] = {
    session.get("gameChoice")
  }

  def clearRPSChoice(implicit session: Session): Session = {
    session.-("gameChoice")
  }

  def restart(implicit session: Session): Session = {
    session.-("gameChoice").-("gameMode")
  }

  def ensureValidRPSSelection(choice: String, gameMode: GameMode): Boolean = {
    gameMode.gameModeChoices.exists(_.key == choice)
  }

  def ensureValidGameMode(choice: String): Boolean = {
    gameModes.exists(_.key == choice)
  }
}
