package stratego.model.gridComponent

import stratego.model.engineComponent.GameState.GameState
import stratego.model.gridComponent.FieldType.FieldType
import stratego.model.playerComponent.Player

trait FieldInterface {
  def setFigure(figure: Option[Figure]): Field
  def getFieldType(): FieldType
  def getFigure(): Option[Figure]
  def toStringTUI(gameState: GameState, activePlayer: Player): String
  def toStringGUI(gameState: GameState, activePlayer: Player): String
}
