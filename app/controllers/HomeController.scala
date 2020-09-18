package controllers

import javax.inject._
import play.api.i18n.I18nSupport
import play.api.mvc._
import play.api.Logging


@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with I18nSupport with Logging {

  def index(): Action[AnyContent] = Action { implicit request: RequestHeader =>
    logger.info("hello")
    Ok(views.html.index())
  }

}
