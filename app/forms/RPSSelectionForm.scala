package forms

import models.RPSChoice
import play.api.data.Forms._
import play.api.data._

class RPSSelectionForm {

  def apply(): Form[RPSChoice] = Form(
    mapping("rps.selection" -> nonEmptyText)
    (RPSChoice.apply)(RPSChoice.unapply)
  )
}
