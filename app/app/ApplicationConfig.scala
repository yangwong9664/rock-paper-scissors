package app

import javax.inject.Singleton

import com.typesafe.config.{ Config, ConfigFactory }

@Singleton
class ApplicationConfig extends AppConfig {
  lazy val config: Config = ConfigFactory.load()
  lazy val appName: String = config.getString("app.name")
}
