package forms

import models.RPS
import models.game.RPSChoice
import play.api.data.Forms._
import play.api.data._
import play.api.mvc.Session

object RPSSelectionForm {

  def rpsSelectionForm(implicit session: Session): Form[RPSChoice] = Form(
    mapping("rps.selection" -> nonEmptyText
      .verifying("error.badChoice", choice => RPS.ensureValidRPSSelection(choice, RPS.getGameMode.get)))
      //controller checks game mode exists, .get here is okay for now
    (RPSChoice.apply)(RPSChoice.unapply)
  )
}
