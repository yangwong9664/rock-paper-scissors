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

  implicit def stringToRPS(string: String): RPS = {
    string match {
      case Rock.key => Rock
      case Paper.key => Paper
      case Scissors.key => Scissors
    }
  }
}
