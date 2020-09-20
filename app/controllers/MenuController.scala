package controllers

import javax.inject._

import forms.GameModeSelectionForm._
import models.RPS
import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.mvc._

import scala.concurrent.Future

@Singleton
class MenuController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with I18nSupport with Logging {

  def menu(): Action[AnyContent] = Action { implicit request =>
    Ok(views.html.menu(gameModeSelectionForm, RPS.gameModes, RPS.getCurrentGameMode))
  }

  def submitMenu(): Action[AnyContent] = Action.async { implicit request =>
    gameModeSelectionForm.bindFromRequest().fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.menu(formWithErrors, RPS.gameModes, RPS.getCurrentGameMode)))
      },
      rpsSelection => {
        logger.info(rpsSelection.choice)
        Future.successful(Redirect(routes.GameController.game(None))
          .withSession(request.session. +("gameMode", rpsSelection.choice))) //TODO validate before adding to session
      }
    )
  }

}
