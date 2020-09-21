package models

import play.api.libs.json.Json

case class SessionModel(
                         gameMode: Option[String],
                         gameChoiceOne: Option[String],
                         gameChoiceTwo: Option[String],
                         gameSpectator: Option[String]
                       )

object SessionModel {
  implicit val format = Json.format[SessionModel]
}
