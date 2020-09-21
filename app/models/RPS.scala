package models

import scala.language.implicitConversions

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

  def ensureValidRPSSelection(choice: String, gameMode: GameMode): Boolean = {
    gameMode.gameModeChoices.exists(_.key == choice)
  }

  def ensureValidGameMode(choice: String): Boolean = {
    gameModes.exists(_.key == choice)
  }

  implicit def stringToRPS(string: String): RPS = {
    string match {
      case Rock.key => Rock
      case Paper.key => Paper
      case Scissors.key => Scissors
    }
  }
}
