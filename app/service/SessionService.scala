package service

import app.GameConfig._
import models.game.GameModeModel
import models.{SessionCookieName, SessionModel}
import play.api.libs.json.Json
import play.api.mvc.Session

object SessionService {
  //service that handles all variables inside Session

  //check is there is a session, create new if non-existant
  def ensureSession(implicit session: Session): Session = {
    session.get(SessionCookieName.key)
      .fold(session. +(SessionCookieName.key, Json.toJson(SessionModel()).toString()): Session){ _ => session}
  }

  //update session variables
  def updateSession(sessionModel: SessionModel)(implicit session: Session): Session = {
    session. +(SessionCookieName.key, Json.toJson(sessionModel).toString())
  }

  //check is user has session
  def validateSession(implicit session: Session): Option[SessionModel] = {
    session.get(SessionCookieName.key).map(_ => sessionToSessionModel(session))
  }

  //get session model from session
  def sessionToSessionModel(session: Session): SessionModel = {
    session.get(SessionCookieName.key).map(Json.parse(_).as[SessionModel]).getOrElse(SessionModel())
  }

  def getGameMode(gameMode: String): Option[GameModeModel] = {
    gameModes.find(_.key == gameMode)
  }

  def getGameMode(gameMode: Option[String]): Option[GameModeModel] = {
    gameMode.flatMap(gameMode => gameModes.find(_.key == gameMode))
  }

  def setGameMode(choice: String, sessionModel: SessionModel): SessionModel = {
    getGameMode(sessionModel.gameMode) match {
      case Some(currentMode) if currentMode.key == choice => sessionModel
      case _ => sessionModel.copy(gameMode = Some(choice), gameChoiceOne = None, gameChoiceTwo = None)
    }
    //this allows a user to have their game persist in session, unless they switch game modes
  }

  def setSpectatorMode(choice: String, sessionModel: SessionModel): SessionModel = {
    sessionModel.gameSpectator match {
      case Some(spectator) if spectator == choice => sessionModel
      case _ => sessionModel.copy(gameSpectator = Some(choice), gameChoiceOne = None, gameChoiceTwo = None)
    }
    //checks if AI vs AI is enabled, currently a Boolean string, could be refactored
  }

}
