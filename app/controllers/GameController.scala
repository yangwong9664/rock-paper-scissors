package controllers

import javax.inject._

import forms.RPSSelectionForm._
import models.RPS
import play.api.Logging
import play.api.data.FormError
import play.api.i18n.I18nSupport
import play.api.mvc._
import play.filters.csrf._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class GameController @Inject()(cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) with I18nSupport with Logging {

  def game(): Action[AnyContent] = { Action { implicit request =>
      RPS.getGameMode match {
        case Some(gameMode) => Ok(views.html.game(rpsSelectionForm, Some(gameMode), RPS.getRPSChoice))
        case None => Redirect(routes.MenuController.menu())
      }
    }
  }

  def submitGame(): Action[AnyContent] = Action.async { implicit request =>
    rpsSelectionForm.bindFromRequest().fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.game(formWithErrors, RPS.getGameMode(request.session), None)))
      },
      rpsSelection => {
        logger.info(rpsSelection.choice)
        RPS.setRPSChoice(rpsSelection.choice, RPS.getGameMode.get) match { //TODO .get
          case Some(validSession) => Future(Redirect(routes.GameController.game()).withSession(validSession))
          case None => Future.successful(
            BadRequest(views.html.game(rpsSelectionForm.withError(FormError("error","invalid selection")), RPS.getGameMode(request.session), None)))
        }

      }
    )
  }

}
