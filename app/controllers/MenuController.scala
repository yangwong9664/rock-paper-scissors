package controllers

import javax.inject._

import forms.GameModeSelectionForm._
import models.{GameSpectator, RPS}
import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.mvc._
import service.SessionService._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class MenuController @Inject()(cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) with I18nSupport with Logging {

  //TODO .get session model refactor

  def menu(): Action[AnyContent] = Action { implicit request =>
    val sessionObject = ensureSession
    Ok(views.html.menu(gameModeSelectionForm, RPS.gameModes, getSessionModel(sessionObject).gameSpectator))
      .withSession(sessionObject)
  }

  def submitMenu(): Action[AnyContent] = Action.async { implicit request =>
    val sessionObject = getSessionModel
    gameModeSelectionForm.bindFromRequest().fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.menu(formWithErrors, RPS.gameModes)))
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
