package controllers

import javax.inject._

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
    getSessionModel match {
      case SessionModel(None,_,_,_) => Redirect(routes.MenuController.menu())
      case SessionModel(Some(gameMode),choiceOne, choiceTwo, spectator) =>
        val gameModeModel = getGameMode(gameMode)
        Ok(views.html.game(rpsSelectionForm(gameModeModel.get.gameModeChoices), gameModeModel, choiceOne, choiceTwo,
          gameResult(choiceOne, choiceTwo, getRuleset(gameModeModel.get)),spectator))
    }
  }
  }

  def submitGame(): Action[AnyContent] = Action.async { implicit request =>
    getSessionModel match {
      case SessionModel(None, _,_,_) => Future(Redirect(routes.MenuController.menu()))
      case SessionModel(Some(gameMode),choiceOne, choiceTwo, spectator) =>
        val gameModeModel = getGameMode(gameMode)
        rpsSelectionForm(gameModeModel.get.gameModeChoices).bindFromRequest().fold(
          formWithErrors => {
            Future.successful(BadRequest(views.html.game(formWithErrors, gameModeModel, choiceOne, choiceTwo, None, spectator)))
          },
          rpsSelection => {
            val updatedModel = doGame(gameMode, Some(rpsSelection.choice))
            Future(Redirect(routes.GameController.game()).withSession(updateSession(updatedModel)))
          }
        )
    }
  }

  def spectate(): Action[AnyContent] = Action.async { implicit request =>
    getSessionModel match {
      case SessionModel(Some(gameMode),_, _, Some(_)) =>
        val updatedModel = doGame(gameMode, None)
        Future(Redirect(routes.GameController.game()).withSession(updateSession(updatedModel)))
      case _ => Future(Redirect(routes.MenuController.menu()))
    }
  }
  private def doGame(gameMode: String, humanPlayerChoice: Option[String])(implicit request: Request[AnyContent]): SessionModel = {
    val gameModeModel = getGameMode(gameMode).get
    val playerOneChoose = humanPlayerChoice.getOrElse(aiChoice(gameModeModel.gameModeChoices).key)
    val playerTwoChoose = aiChoice(gameModeModel.gameModeChoices).key
    getSessionModel.copy(gameChoiceOne = Some(playerOneChoose), gameChoiceTwo = Some(playerTwoChoose))
  }

}
