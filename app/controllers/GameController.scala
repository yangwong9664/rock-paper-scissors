package controllers

import javax.inject._

import app.GameConfig._
import forms.RPSSelectionForm._
import models.SessionModel
import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.mvc._
import service.GameLogicEngine._
import service.SessionService._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class GameController @Inject()(cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) with I18nSupport with Logging {

  def game(): Action[AnyContent] = { Action { implicit request =>
    validateSession match {
      case Some(SessionModel(Some(gameMode),choiceOne, choiceTwo, spectator)) =>
        val gameModeModel = getGameMode(gameMode)
        Ok(views.html.game(rpsSelectionForm(gameModeModel.get.gameModeChoices), gameModeModel, choiceOne, choiceTwo,
          gameResult(choiceOne, choiceTwo, getRuleset(gameModeModel.get)),spectator))
      case _=> Redirect(routes.MenuController.menu())
    }
  }
  }

  def submitGame(): Action[AnyContent] = Action.async { implicit request =>
    validateSession match {
      case Some(SessionModel(Some(gameMode),choiceOne, choiceTwo, spectator)) =>
        val gameModeModel = getGameMode(gameMode)
        rpsSelectionForm(gameModeModel.get.gameModeChoices).bindFromRequest().fold(
          formWithErrors => {
            Future.successful(BadRequest(views.html.game(formWithErrors, gameModeModel, choiceOne, choiceTwo, None, spectator)))
          },
          rpsSelection => {
            val updatedModel = doGame(gameMode, Some(rpsSelection.choice), sessionToSessionModel(request.session))
            Future(Redirect(routes.GameController.game()).withSession(updateSession(updatedModel)))
          }
        )
      case _ => Future(Redirect(routes.MenuController.menu()))
    }
  }

  def spectate(): Action[AnyContent] = Action.async { implicit request =>
    validateSession match {
      case Some(SessionModel(Some(gameMode),_, _, Some(_))) =>
        val updatedModel = doGame(gameMode, None, sessionToSessionModel(request.session))
        Future(Redirect(routes.GameController.game()).withSession(updateSession(updatedModel)))
      case _ => Future(Redirect(routes.MenuController.menu()))
    }
  }

  private def doGame(gameMode: String, humanPlayerChoice: Option[String], sessionModel: SessionModel)(implicit request: Request[AnyContent]): SessionModel = {
    val gameModeModel = getGameMode(gameMode).get //TODO refactor
    val playerOneChoose = humanPlayerChoice.getOrElse(aiChoice(gameModeModel.gameModeChoices).key)
    val playerTwoChoose = aiChoice(gameModeModel.gameModeChoices).key
    sessionModel.copy(gameChoiceOne = Some(playerOneChoose), gameChoiceTwo = Some(playerTwoChoose))
  }

}
