package controllers

import javax.inject._

import forms.RPSSelectionForm._
import models.RPS
import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.mvc._

import scala.concurrent.Future

@Singleton
class GameController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with I18nSupport with Logging {

  def game(choice: Option[String]): Action[AnyContent] = Action { implicit request =>
    RPS.getCurrentGameMode match {
      case Some(gameMode) => Ok(views.html.game(rpsSelectionForm,Some(gameMode), choice))
      case None => Redirect(routes.MenuController.menu())
    }
  }

  def submitGame(): Action[AnyContent] = Action.async { implicit request =>
    rpsSelectionForm.bindFromRequest().fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.game(formWithErrors, RPS.getCurrentGameMode(request.session), None)))
      },
      rpsSelection => {
        logger.info(rpsSelection.choice)
        Future.successful(Redirect(routes.GameController.game(Some(rpsSelection.choice))))
      }
    )
  }

}
