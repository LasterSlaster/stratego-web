package stratego.model.gridComponent

import stratego.model.gridComponent.FieldType._
import stratego.model.engineComponent.GameState
import stratego.model.engineComponent.GameState.GameState
import stratego.model.playerComponent.Player

case class Field(fieldType: FieldType = EMPTY_FIELD, figure: Option[Figure]) extends FieldInterface() {
  override def setFigure(figure: Option[Figure]): Field = copy(fieldType, figure)

  override def toStringTUI(gameState: GameState, player: Player): String = {
    if (figure eq None) fieldType.toString
    else if(gameState == GameState.END) figure.get.toString
    else if (figure.get.player != player) "[??]"
    else figure.get.toString
  }
  override def getFieldType(): FieldType = fieldType
  override def getFigure(): Option[Figure] = figure

  override def toStringGUI(gameState: GameState, activePlayer: Player): String = {
    if (figure eq None) ""
    else if(gameState == GameState.END) figure.get.figureType.toString+ "_" + figure.get.player.name
    else if (figure.get.player != activePlayer) "backside_" + figure.get.player.name
    else figure.get.figureType.toString+ "_" + figure.get.player.name
  }
}
