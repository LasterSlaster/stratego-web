package controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AnyContent, BaseController, ControllerComponents, Request}
import stratego.model.engineComponent.{GameEngine, GameEngineInterface}

@Singleton
class WebTUIController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  var gameEngine: GameEngineInterface = GameEngine()
  //TODO: rückgabe von gameEngine ist nur der neue Zustand aber nicht das Event also was die View vom Modell ausgeben soll -> Controller entscheidet aufrund von methode/route welche Methode auf ConsoleView aufgerufen werden soll und übergibt neue gameEngine
  // oder GameEngine wird um attribut event erweitert oder einfach evaluierung von gameSTate und gameSTatus

  def index() = Action { implicit request: Request[AnyContent] =>
    gameEngine = gameEngine.startNewGame
    Ok(views.html.index())
  }

  def startNewGame() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def placeFigure(id: String) = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def moveFigure(id: String) = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }
}
