package controllers

import javax.inject._

import forms.GameModeSelectionForm._
import models.RPS
import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.mvc._
import service.SessionService._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class MenuController @Inject()(cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) with I18nSupport with Logging {

  def menu(): Action[AnyContent] = Action { implicit request =>
    Ok(views.html.menu(gameModeSelectionForm, RPS.gameModes, getGameMode, getSpectatorMode))
  }

  def submitMenu(): Action[AnyContent] = Action.async { implicit request =>
    gameModeSelectionForm.bindFromRequest().fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.menu(formWithErrors, RPS.gameModes, getGameMode)))
      },
      gameModeSelection => {
        //TODO refactor
        logger.info(gameModeSelection.spectatorMode.toString)
        val gameModeUpdate = setGameMode(gameModeSelection.choice)
        val spectatorModeUpdate = spectatorMode(gameModeSelection.spectatorMode)(gameModeUpdate)
        Future(Redirect(routes.GameController.game()).withSession(spectatorModeUpdate))
      }
    )
  }

  private def spectatorMode(spectatorModeSelection: Boolean)(implicit session: Session) = {
    (spectatorModeSelection, getSpectatorMode) match {
      case (true, false) => setSpectatorMode(true)(session)
      case (false, true) => unsetSpectatorMode(true)(session)
      case _ => session
    }
  }

  def restart(): Action[AnyContent] = Action { implicit request =>
    Redirect(routes.MenuController.menu()).withNewSession
  }

}
