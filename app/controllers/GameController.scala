package controllers

import forms.RPSSelectionForm
import javax.inject._
import models.RPS
import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.mvc._


@Singleton
class GameController @Inject()(cc: ControllerComponents, rpsSelectionForm: RPSSelectionForm) extends AbstractController(cc) with I18nSupport with Logging {

  def game(): Action[AnyContent] = Action { implicit request =>
    val x: Seq[String] = RPS.classicGameMode.map(_.formName)

    Ok(views.html.game(x))
  }

}
