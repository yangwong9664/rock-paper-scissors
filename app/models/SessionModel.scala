package models

import play.api.libs.json.{Json, OFormat}

case class SessionModel(
                         gameMode: Option[String] = None,
                         gameChoiceOne: Option[String] = None,
                         gameChoiceTwo: Option[String] = None,
                         gameSpectator: Option[String] = None
                       )

object SessionModel {
  implicit val format: OFormat[SessionModel] = Json.format[SessionModel]
}
