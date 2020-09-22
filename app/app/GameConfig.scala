package app

import models.{Paper, RPS, Rock, Scissors}
import models.game.{GameModeModel, RPSResult}

object GameConfig {

  lazy val classicGameMode: GameModeModel = GameModeModel("Rock, Paper, Scissors, Classic Mode", "classic",Seq(Rock, Paper, Scissors))
  lazy val rockGameMode: GameModeModel = GameModeModel("Rock Rock, High Strategy Mode", "rock", Seq(Rock, Rock))

  lazy val gameModes: Seq[GameModeModel] = Seq(classicGameMode, rockGameMode)
  //Expansions can be added here

  //could create a CSV file matrix, but this is MVP
  lazy val classicRules: Map[RPS, RPSResult] = Map[RPS, RPSResult](
    Rock -> RPSResult(Seq(Scissors), Seq(Paper)),
    Paper -> RPSResult(Seq(Rock), Seq(Scissors)),
    Scissors -> RPSResult(Seq(Paper), Seq(Rock))
  )

  lazy val rockRules: Map[RPS, RPSResult] = Map[RPS, RPSResult](
    Rock -> RPSResult(Seq.empty, Seq.empty)
  )

  def getRuleset(gameMode: GameModeModel): Map[RPS, RPSResult] = {
    gameMode.key match {
      case "classic" => classicRules
      case "rock" => rockRules
    }
  }

}
