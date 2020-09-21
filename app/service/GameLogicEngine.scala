package service

import models.game.{GameModeModel, RPSResult}
import models._
import RPS._

import scala.util.Random

object GameLogicEngine {

  def gameResult(playerOne: Option[String], playerTwo: Option[String], ruleset: Map[RPS, RPSResult]): Option[Boolean] = {
    (playerOne,playerTwo) match {
      case (Some(one), Some(two)) =>
        calculateGameResult(one,two, ruleset)
      case _ => None
    }
  }

  //returns a winning player, or None if there's a draw
  def calculateGameResult(challengerOneChoice: RPS, challengerTwoChoice: RPS, ruleset: Map[RPS, RPSResult]): Option[Boolean] = {
    ruleset(challengerOneChoice) match {
      case result if result.win.contains(challengerTwoChoice) => //Player1 wins
        Some(true)
      case result if result.lose.contains(challengerTwoChoice) => //Player1 loses
        Some(false)
      case _ => //Draw
        None
    }
  }

  def getRuleset(gameMode: GameModeModel): Map[RPS, RPSResult] = {
    gameMode.key match {
      case "classic" => classicRules
      case "rock" => rockRules
    }
  }

  def aiChoice(choices: Seq[RPS]): RPS = {
    val rand = Random.nextInt(choices.length)
    choices(rand)
  }

  //could create a CSV file matrix, but this is MVP
  val classicRules = Map[RPS, RPSResult](
    Rock -> RPSResult(Seq(Scissors), Seq(Paper)),
    Paper -> RPSResult(Seq(Rock), Seq(Scissors)),
    Scissors -> RPSResult(Seq(Paper), Seq(Rock))
  )

  val rockRules = Map[RPS, RPSResult](
    Rock -> RPSResult(Seq.empty, Seq.empty)
  )




}
