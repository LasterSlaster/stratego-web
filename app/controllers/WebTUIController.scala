package controllers

import play.api.data.Forms._
import javax.inject.{Inject, Singleton}
import play.api.data.Form
import play.api.mvc.{AnyContent, BaseController, ControllerComponents, Request}
import stratego.model.engineComponent.{GameEngine, GameEngineInterface, GameState}
import stratego.model.gridComponent.{FigureType, Position}
import stratego.view.tui.ConsoleView

@Singleton
class WebTUIController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  var gameEngine: GameEngineInterface = GameEngine()
  val consoleView = new ConsoleView()

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index(consoleView.gameMenuToString))
  }

  def startNewGame() = Action { implicit request: Request[AnyContent] =>
    gameEngine = gameEngine.startNewGame
    val content = consoleView.gameStartToString(gameEngine)
    Ok(views.html.figuresetup(content))
  }

  def startNewDefaultGame() = Action { implicit request: Request[AnyContent] =>
    gameEngine = gameEngine.setUpDefaultGrid
    val content = consoleView.figureSetResultToString(gameEngine)
    Ok(views.html.game(content))
  }

  def quitGame() = Action { implicit request: Request[AnyContent] =>
    gameEngine = gameEngine.quitGame
    val content = consoleView.gameQuitToString(gameEngine)
    Ok(views.html.index(content))
  }

  def placeFigure(id: String) = Action { implicit request: Request[AnyContent] =>
    Form(tuple("row" -> number, "col" -> number)).bindFromRequest.fold(
      formWithErrors =>BadRequest,
      form => {
        val figureType = convertInputToFigureType(id.toInt)
        val position = Position(form._1, form._2)
        gameEngine = gameEngine.setFigure(figureType, position)
        val content = consoleView.figureSetResultToString(gameEngine)
        gameEngine.getGameState match {
          case GameState.FIGHT => Ok(views.html.game(content))
          case GameState.SET_FIGURES => Ok(views.html.figuresetup(content))
        }
      }
    )
  }

  def moveFigure(id: String) = Action { implicit request: Request[AnyContent] =>
    Form(tuple("toRow" -> number, "toCol" -> number)).bindFromRequest.fold(
      formWithErrors =>BadRequest,
      form => {
        val split = id.split('-')
        val row = split(0)
        val col = split(1)
        val fromPosition = Position(row.toInt, col.toInt)
        val toPosition = Position(form._1, form._2)
        gameEngine = gameEngine.moveFigure(fromPosition, toPosition)
        val content = consoleView.moveResultToString(gameEngine)
        gameEngine.getGameState match {
          case GameState.FIGHT => Ok(views.html.game(content))
          case GameState.END => Ok(views.html.index(content))
        }
      }
    )
  }

  private def convertInputToFigureType(number: Int): FigureType.FigureType = {
    number match {
      case 1 => FigureType.BOMB
      case 2 => FigureType.MARSHAL
      case 3 => FigureType.GENERAL
      case 4 => FigureType.COLONEL
      case 5 => FigureType.MAJOR
      case 6 => FigureType.CAPTAIN
      case 7 => FigureType.LIEUTENANT
      case 8 => FigureType.SERGEANT
      case 9 => FigureType.MINER
      case 10 => FigureType.SCOUT
      case 11 => FigureType.SPY
      case 12 => FigureType.FLAG
    }
  }
}
