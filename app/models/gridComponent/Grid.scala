package stratego.model.gridComponent

import stratego.model.engineComponent.GameState.GameState
import stratego.model.playerComponent.Player
import scala.stratego.model.gridComponent.Matrix
import scala.math.sqrt

case class Grid (matrix: Matrix[Field] = new Matrix[Field](10, Field(FieldType.EMPTY_FIELD, None))) extends GridInterface {
  val size: Int = matrix.size
  val sizeRowCol: Int = sqrt(size).toInt

  def getField(position: Position): FieldInterface = matrix.getField(position.row, position.col)

  def createNewGrid(): GridInterface = {
    Grid().createPlayableGrid()
  }

  def createPlayableGrid(): GridInterface = {
    var newMatrix = matrix
    for {
      row <- 0 until 4
      col <- 0 until 10
    } newMatrix = newMatrix.replaceField(row, col, Field(FieldType.A_SIDE, None))
    for {
      row <- 6 until 10
      col <- 0 until 10
    } newMatrix = newMatrix.replaceField(row, col, Field(FieldType.B_SIDE, None))

    copy(newMatrix.replaceField(4, 2, Field(FieldType.NO_FIELD, None))
      .replaceField(4, 3, Field(FieldType.NO_FIELD, None))
      .replaceField(5, 2, Field(FieldType.NO_FIELD, None))
      .replaceField(5, 3, Field(FieldType.NO_FIELD, None))
      .replaceField(4, 6, Field(FieldType.NO_FIELD, None))
      .replaceField(4, 7, Field(FieldType.NO_FIELD, None))
      .replaceField(5, 6, Field(FieldType.NO_FIELD, None))
      .replaceField(5, 7, Field(FieldType.NO_FIELD, None)))
  }

  def assignField(position: Position, figure: Option[Figure]): GridInterface= {
    val field = this.matrix.getField(position.row, position.col)
    copy(this.matrix.replaceField(position.row, position.col, Field(field.getFieldType(), figure)))
  }

  def move(from: Position, to: Position): GridInterface = {
    val figure = getField(from).getFigure()
    assignField(from, None).assignField(to, figure)
  }

  // private def noFieldAssignment(row: Int, col: Int, grid: GridInterface): GridInterface = {var grid = this; copy(grid.matrix.replaceField(row, col, Field(FieldType.NO_FIELD, None)))}

  def toStringTUI(gameState: GameState, activePlayer: Player): String = {
    var stringGrid = "\n"
    for {
      row <- 0 until 10
      col <- 0 until 10
    } {
      stringGrid += getField(Position(row, col)).toStringTUI(gameState, activePlayer)
      if (col == 9) stringGrid += " " + row +"\n"
    }
    stringGrid + "- 0 - 1 - 2 - 3 - 4 - 5 - 6 - 7 - 8 - 9";
  }

}
