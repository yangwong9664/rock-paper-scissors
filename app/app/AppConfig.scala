package app

import com.typesafe.config.Config

trait AppConfig {
  val config: Config
  val appName: String
}
