package controllers

import javax.inject._

import forms.GameModeSelectionForm._
import models.RPS
import play.api.Logging
import play.api.data.FormError
import play.api.i18n.I18nSupport
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class MenuController @Inject()(cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) with I18nSupport with Logging {

  def menu(): Action[AnyContent] = Action { implicit request =>
    Ok(views.html.menu(gameModeSelectionForm, RPS.gameModes, RPS.getGameMode))
  }

  def submitMenu(): Action[AnyContent] = Action.async { implicit request =>
    gameModeSelectionForm.bindFromRequest().fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.menu(formWithErrors, RPS.gameModes, RPS.getGameMode)))
      },
      gameModeSelection => {
        logger.info(gameModeSelection.choice)
        RPS.setGameMode(gameModeSelection.choice) match {
          case Some(validSession) => Future(Redirect(routes.GameController.game())
            .withSession(validSession))
          case None => Future.successful(
            BadRequest(views.html.menu(gameModeSelectionForm.withError(FormError("error","invalid selection")), RPS.gameModes, RPS.getGameMode)))
        }
        //TODO refactor Match in both controllers
      }
    )
  }

  def restart(): Action[AnyContent] = Action { implicit request =>
    Redirect(routes.MenuController.menu()).withSession(RPS.restart)
  }

}
