package stratego.model.gridComponent

import stratego.model.engineComponent.GameState.GameState
import stratego.model.playerComponent.Player

trait GridInterface {

  def createNewGrid(): GridInterface
  def createPlayableGrid(): GridInterface
  def getField(position: Position): FieldInterface
  def size(): Int
  def assignField(position: Position, figure: Option[Figure]): GridInterface
  def move(from: Position, to: Position): GridInterface
  def toStringTUI(gameState: GameState, activePlayer: Player): String
}
