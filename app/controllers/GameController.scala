package controllers

import javax.inject._

import models.RPS
import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.mvc._
import forms.RPSSelectionForm._

import scala.concurrent.Future

@Singleton
class GameController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with I18nSupport with Logging {

  val gameMode: Seq[String] = RPS.classicGameMode.map(_.formName)

  def game(): Action[AnyContent] = Action { implicit request =>

    Ok(views.html.game(rpsSelectionForm,gameMode))
  }

  def submitGame(): Action[AnyContent] = Action.async { implicit request =>
    rpsSelectionForm.bindFromRequest().fold(
      formWithErrors => {
        println(formWithErrors.errors.toString())
        Future.successful(BadRequest(views.html.game(formWithErrors, gameMode)))
      },
      rpsSelection => {
        println(rpsSelection.choice)
        Future.successful(Redirect(routes.GameController.game()))
      }
    )
  }

}
