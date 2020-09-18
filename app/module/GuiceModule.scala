package module

import javax.inject.Singleton

import app.{ AppConfig, ApplicationConfig }
import com.google.inject.AbstractModule

@Singleton
class GuiceModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[AppConfig]) to classOf[ApplicationConfig]

  }
}
