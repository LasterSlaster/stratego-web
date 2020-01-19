package stratego.model.engineComponent

import stratego.gameEngine.GameStatus.GameStatus
import stratego.model.engineComponent.GameState.GameState
import stratego.model.gridComponent.{Figure, FigureSet, GridInterface, Position}
import stratego.model.gridComponent.FigureType.FigureType
import stratego.model.playerComponent.Player

import scala.swing.Publisher
import scala.swing.event.Event

trait GameEngineInterface extends Publisher {
  def startNewGame: GameEngineInterface
  def quitGame: GameEngineInterface
  def startBattle: GameEngineInterface
  def changePlayer: GameEngineInterface
  def setFigure(figureType: FigureType, position: Position): GameEngineInterface
  def moveFigure(from: Position, to: Position): GameEngineInterface
  def getFigureSetActivePlayer: FigureSet
  def getFigure(position: Position): Option[Figure]
  def getGrid: GridInterface
  def getGameState: GameState
  def getActivePlayer: Player
  def getWinner: Option[Player]
  def getStatusLine: GameStatus
  def setUpDefaultGrid: GameEngineInterface
  def getFieldStringGUI(position:Position): String
  def gridToString: String
}

case class FigureDeletedEvent(gameEngine: GameEngineInterface) extends Event
case class GameStartedEvent(gameEngine: GameEngineInterface) extends Event
case class GameQuitEvent(gameEngine: GameEngineInterface) extends Event
case class WinnerEvent(gameEngine: GameEngineInterface) extends Event
case class MoveFigureEvent(gameEngine: GameEngineInterface) extends Event
case class FigureSetEvent(gameEngine: GameEngineInterface) extends Event
case class AttackEvent(gameEngine: GameEngineInterface) extends Event
case class InvalidMoveEvent(gameEngine: GameEngineInterface) extends Event
case class ChangePlayerEvent(gameEngine: GameEngineInterface) extends Event
case class Init() extends Event

//TODO: remove these two events
case class GameQuit() extends Event
case class GameChanged() extends Event
