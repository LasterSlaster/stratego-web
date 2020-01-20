package stratego.model.engineComponent

import stratego.gameEngine.GameStatus.GameStatus
import stratego.model.engineComponent.GameState.GameState
import stratego.model.gridComponent.FigureType.FigureType
import stratego.model.gridComponent.{Figure, FigureSet, GridInterface, Position}
import stratego.model.playerComponent.Player

class GameEngineProxy(var gameEngine: GameEngineInterface) extends GameEngineInterface {

  //propagate events
  listenTo(gameEngine)
  deafTo(this)
  reactions += {
      case event: GameStartedEvent => this.publish(event)
      case event: FigureSetEvent => this.publish(event)
      case event: MoveFigureEvent => this.publish(event)
      case event: InvalidMoveEvent => this.publish(event)
      case event: AttackEvent => this.publish(event)
      case event: WinnerEvent => this.publish(event)
      case event: GameQuitEvent => this.publish(event)
  }

  def startNewGame: GameEngineInterface =  {
    remap(gameEngine.startNewGame)
  }

  def quitGame: GameEngineInterface = {
    remap(gameEngine.quitGame)
  }

  def startBattle: GameEngineInterface = {
    remap(gameEngine.startBattle)
  }
  def changePlayer: GameEngineInterface = {
    remap(gameEngine.changePlayer)
  }

  def setFigure(figureType: FigureType, position: Position): GameEngineInterface = {
    remap(gameEngine.setFigure(figureType, position))
  }

  def moveFigure(from: Position, to: Position): GameEngineInterface = {
    remap(gameEngine.moveFigure(from, to))
  }

  private def remap(newGameEngine: GameEngineInterface): GameEngineInterface = {
    deafTo(gameEngine)
    listenTo(newGameEngine)
    gameEngine = newGameEngine
    gameEngine
  }

  def gridToString: String = gameEngine.gridToString

  def getFigureSetActivePlayer: FigureSet = gameEngine.getFigureSetActivePlayer

  def getFigure(position: Position): Option[Figure] = gameEngine.getFigure(position)

  def getGrid: GridInterface = gameEngine.getGrid

  def getGameState: GameState = gameEngine.getGameState

  def getActivePlayer: Player = gameEngine.getActivePlayer

  def getWinner: Option[Player] = gameEngine.getWinner

  def getStatusLine: GameStatus = gameEngine.getStatusLine

  def getFieldStringGUI(position: Position): String = gameEngine.getFieldStringGUI(position)
}
