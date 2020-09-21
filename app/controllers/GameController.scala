package controllers

import javax.inject._

import forms.RPSSelectionForm._
import models.RPS
import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.mvc._
import service.GameLogicEngine._
import service.SessionService._
import models.RPS._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class GameController @Inject()(cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) with I18nSupport with Logging {

  def game(): Action[AnyContent] = { Action { implicit request =>
    getGameMode match {
      case Some(gameMode) =>
        //TODO refactor
        Ok(views.html.game(rpsSelectionForm, Some(gameMode), getRPSChoice, getRPSChoiceTwo,
          gameResult(getRPSChoice, getRPSChoiceTwo, getRuleset(gameMode)),getSpectatorMode))
      case None => Redirect(routes.MenuController.menu())
    }
  }
  }

  //TODO session is checked for existence in GET, possibly could add another check here if there's time, to avoid .gets
  def submitGame(): Action[AnyContent] = Action.async { implicit request =>
    logger.info(s"Spectator Mode: $getSpectatorMode")
    rpsSelectionForm.bindFromRequest().fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.game(formWithErrors, getGameMode)))
      },
      rpsSelection => {
        logger.info(rpsSelection.choice)

        //TODO refactor
        val playerTwoChoose = aiChoice(getGameMode.get.gameModeChoices)
        val playerTwoChooseUpdate = setRPSChoiceTwo(playerTwoChoose.key, getGameMode.get)
        val playerOneChoose: RPS = rpsSelection.choice
        val playerOneChooseUpdate = setRPSChoice(playerOneChoose.key, getGameMode.get)(playerTwoChooseUpdate)
        Future(Redirect(routes.GameController.game()).withSession(playerOneChooseUpdate))
      }
    )
  }

  def spectate(): Action[AnyContent] = Action.async { implicit request =>
    logger.info(s"Spectator Mode: $getSpectatorMode")
    //TODO refactor
    val playerTwoChoose = aiChoice(getGameMode.get.gameModeChoices)
    val playerTwoChooseUpdate = setRPSChoiceTwo(playerTwoChoose.key, getGameMode.get)
    val playerOneChoose: RPS = aiChoice(getGameMode.get.gameModeChoices)
    val playerOneChooseUpdate = setRPSChoice(playerOneChoose.key, getGameMode.get)(playerTwoChooseUpdate)
    Future(Redirect(routes.GameController.game()).withSession(playerOneChooseUpdate))
  }

}
