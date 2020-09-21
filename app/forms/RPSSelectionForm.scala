package forms

import models.RPS
import models.game.RPSChoice
import play.api.data.Forms._
import play.api.data._
import play.api.mvc.Session

object RPSSelectionForm {

  def rpsSelectionForm(gameModeChoices: Seq[RPS])(implicit session: Session): Form[RPSChoice] = Form(
    mapping("rps.selection" -> nonEmptyText
      .verifying("error.badChoice", choice => gameModeChoices.exists(_.key == choice)))
      //controller checks game mode exists, .get here is okay for now
    (RPSChoice.apply)(RPSChoice.unapply)
  )
}
