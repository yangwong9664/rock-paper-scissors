package service

import models.RPS.gameModes
import models.game.GameModeModel
import models.{SessionCookieName, SessionKeys, SessionModel}
import play.api.Logging
import play.api.libs.json.Json
import play.api.mvc.Session

object SessionService extends Logging {
  //service that handles all variables inside Session

  def ensureSession(implicit session: Session): Session = {
    session.get(SessionCookieName.key) match {
      case Some(_) => session
      case None => session. +(SessionCookieName.key, Json.toJson(SessionModel()).toString())
    }
  }

  def updateSession(sessionModel: SessionModel)(implicit session: Session): Session = {
    session. +(SessionCookieName.key, Json.toJson(sessionModel).toString())
  }

  def getSessionModel(implicit session: Session): SessionModel = {
    val sessionModel = sessionToSessionModel(ensureSession)
    println(Json.prettyPrint(Json.toJson(sessionModel)))
    sessionModel
  }

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
  }

}
