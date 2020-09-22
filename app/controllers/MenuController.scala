package controllers

import javax.inject._

import app.GameConfig._
import forms.GameModeSelectionForm._
import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.mvc._
import service.SessionService._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class MenuController @Inject()(cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) with I18nSupport with Logging {

  def menu(): Action[AnyContent] = Action { implicit request =>
    val sessionObject = ensureSession
    Ok(views.html.menu(gameModeSelectionForm, gameModes, sessionToSessionModel(sessionObject).gameSpectator))
      .withSession(sessionObject)
  }

  def submitMenu(): Action[AnyContent] = Action.async { implicit request =>
    val sessionObject = sessionToSessionModel(request.session)
    gameModeSelectionForm.bindFromRequest().fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.menu(formWithErrors, gameModes)))
      },
      gameModeSelection => {
        val gameModeUpdate = setGameMode(gameModeSelection.choice,sessionObject)
        val spectatorUpdate = setSpectatorMode(gameModeSelection.spectatorMode.toString, gameModeUpdate)
        Future(Redirect(routes.GameController.game()).withSession(updateSession(spectatorUpdate)))
      }
    )
  }

  def restart(): Action[AnyContent] = Action { implicit request =>
    Redirect(routes.MenuController.menu()).withNewSession
  }

}
