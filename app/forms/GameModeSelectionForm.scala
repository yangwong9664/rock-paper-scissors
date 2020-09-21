package forms

import models.RPS.gameModes
import models.menu.GameModeChoice
import play.api.data.Forms._
import play.api.data._
import play.api.mvc.Session

object GameModeSelectionForm {

  def gameModeSelectionForm(implicit session: Session): Form[GameModeChoice] = Form(
    mapping(
      "game.mode.selection" -> nonEmptyText.verifying("error.badChoice", choice => gameModes.exists(_.key == choice)),
      "spectator.mode" -> boolean
    )
    (GameModeChoice.apply)(GameModeChoice.unapply)
  )
}
