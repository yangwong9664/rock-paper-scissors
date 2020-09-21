package models

import models.game.GameModeModel

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

  lazy val classicGameMode: GameModeModel = GameModeModel("Rock, Paper, Scissors, Classic Mode", "classic",Seq(Rock, Paper, Scissors))
  lazy val rockGameMode: GameModeModel = GameModeModel("Rock Rock, High Strategy Mode", "rock", Seq(Rock, Rock))

  lazy val gameModes: Seq[GameModeModel] = Seq(classicGameMode, rockGameMode)
  //Expansions can be added here

  implicit def stringToRPS(string: String): RPS = {
    string match {
      case Rock.key => Rock
      case Paper.key => Paper
      case Scissors.key => Scissors
    }
  }
}
