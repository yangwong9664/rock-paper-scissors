package models.game

import models.RPS

case class GameModeModel(name: String, key: String, gameModeChoices: Seq[RPS])
