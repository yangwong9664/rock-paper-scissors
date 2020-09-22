package services

import app.GameConfig
import models.{Paper, Rock, Scissors}
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import service.GameLogicEngine

class GameLogicEngineSpec extends PlaySpec with GuiceOneAppPerTest with MockitoSugar {

  val service = GameLogicEngine

  "gameResult (classicRules)" must {

    "correctly calculate a Rock vs Paper result for classic mode" in {

      val res = service.gameResult(Some(Rock.key),Some(Paper.key), GameConfig.classicRules)

      res mustBe Some(false)
    }

    "correctly calculate a Rock vs Scissors result for classic mode" in {

      val res = service.gameResult(Some(Rock.key),Some(Scissors.key), GameConfig.classicRules)

      res mustBe Some(true)
    }

    "correctly calculate a Rock vs Rock result for classic mode" in {

      val res = service.gameResult(Some(Rock.key),Some(Rock.key), GameConfig.classicRules)

      res mustBe None
    }
  }

  "gameResult (rockRules)" must {

    "correctly calculate a Rock vs Rock result for rock mode" in {

      val res = service.gameResult(Some(Rock.key),Some(Rock.key), GameConfig.rockRules)

      res mustBe None
    }
  }


}
