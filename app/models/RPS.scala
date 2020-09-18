package models

sealed trait RPS {
  val formName: String
}

case object Rock extends RPS {
  override val formName: String = "rock"
}
case object Paper extends RPS {
  override val formName: String = "paper"
}
case object Scissors extends RPS {
  override val formName: String = "scissors"
}

object RPS {
  val classicGameMode = Seq(Rock, Paper, Scissors)
}

//Expansions can be added here
