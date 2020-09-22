package service

import models.RPS._
import models._
import models.game.RPSResult

import scala.util.Random

object GameLogicEngine {

  def gameResult(playerOne: Option[String], playerTwo: Option[String], ruleset: Map[RPS, RPSResult]): Option[Boolean] = {
    (playerOne,playerTwo) match {
      case (Some(one), Some(two)) => calculateGameResult(one,two, ruleset)
      case _ => None
    }
  }

  //returns a winning player, or None if there's a draw
  private def calculateGameResult(challengerOneChoice: RPS, challengerTwoChoice: RPS, ruleset: Map[RPS, RPSResult]): Option[Boolean] = {
    ruleset(challengerOneChoice) match {
      case result if result.win.contains(challengerTwoChoice) => Some(true) //Player1 wins
      case result if result.lose.contains(challengerTwoChoice) => Some(false) //Player1 loses
      case _ => None //Draw
    }
  }

  def aiChoice(choices: Seq[RPS]): RPS = {
    val rand = Random.nextInt(choices.length)
    choices(rand)
  }
}
