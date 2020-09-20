package forms

import models.RPS
import models.menu.GameModeChoice
import play.api.data.Forms._
import play.api.data._
import play.api.mvc.Session

object GameModeSelectionForm {

  def gameModeSelectionForm(implicit session: Session): Form[GameModeChoice] = Form(
    mapping("game.mode.selection" -> nonEmptyText
      .verifying("error.badChoice", choice => RPS.ensureValidGameMode(choice)))
    (GameModeChoice.apply)(GameModeChoice.unapply)
  )
}
