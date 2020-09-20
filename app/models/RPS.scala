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

  def getCurrentGameMode(implicit session: Session): Option[GameMode] = {
    session.get("gameMode") match {
      case Some(gameMode) => gameModes.find(_.key == gameMode)
      case None => None
    }
  }

  def ensureValidSelection(choice: String, gameMode: GameMode): Boolean = {
    gameMode.gameModeChoices.exists(_.key == choice)
  }

  def ensureValidGameMode(choice: String): Boolean = {
    gameModes.exists(_.key == choice)
  }
}
