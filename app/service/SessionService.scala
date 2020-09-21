package service

import models.GameMode
import models.RPS.gameModes
import play.api.mvc.Session

object SessionService {
  //service that handles all variables inside Session
  //for an improvement, session data would become nested Json for better management

  //TODO put session variable names somewhere else
  //gameMode
  //gameChoice.1
  //gameChoice.2
  //gameUser
  //gameSpectator


  //TODO refactor these methods

  def getRPSChoiceTwo(implicit session: Session): Option[String] = {
    session.get("gameChoice.2")
    //AI player 2 choice
  }

  def setRPSChoiceTwo(choice: String, gameMode: GameMode)(implicit session: Session): Session = {
    session. +("gameChoice.2",choice)
  }

  def clearRPSChoiceTwo(implicit session: Session): Session = {
    session.-("gameChoice.2")
    //remove the last ai choice, e.g for switching game modes
  }

  def getSpectatorMode(implicit session: Session): Boolean = {
    session.get("gameSpectator") match {
      case Some(_) => true
      case None => false
    }
    //checks if player going to play or watch
  }

  def setSpectatorMode(boolean: Boolean)(implicit session: Session): Session = {
    session.+("gameSpectator", boolean.toString).-("gameChoice.1").-("gameChoice.2")
    //enables ai vs ai mode
  }

  def unsetSpectatorMode(boolean: Boolean)(implicit session: Session): Session = {
    session.-("gameSpectator").-("gameChoice.1").-("gameChoice.2")
    //enables ai vs ai mode
  }

  def getGameMode(implicit session: Session): Option[GameMode] = {
    session.get("gameMode") match {
      case Some(gameMode) => gameModes.find(_.key == gameMode)
      case None => None
    }
    //checks that the user submitted value is a valid game mode
  }

  def setGameMode(choice: String)(implicit session: Session): Session = {

    getGameMode match {
      case Some(currentMode) if currentMode.key == choice => session
      case _ => clearRPSChoice. +("gameMode", choice)
    }
    //this allows a user to have their game persist in session, unless they switch game modes
  }

  def getRPSChoice(implicit session: Session): Option[String] = {
    session.get("gameChoice.1")
    //get the last user choice stored in session
  }

  def setRPSChoice(choice: String, gameMode: GameMode)(implicit session: Session): Session = {
    session. +("gameChoice.1",choice)
    //checks that the user choice is valid and corresponds with a game mode
  }

  def clearRPSChoice(implicit session: Session): Session = {
    session.-("gameChoice.1")
    //remove the last user choice, e.g for switching game modes
  }

}
