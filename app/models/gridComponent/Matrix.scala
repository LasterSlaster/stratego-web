package scala.stratego.model.gridComponent

import stratego.model.gridComponent.FieldInterface

case class Matrix[Field](grid: Vector[Vector[FieldInterface]]) {
  def this(size: Int, field: FieldInterface) =
    this(Vector.tabulate(size, size) {(row, col) => field})

  val size: Int = grid.size

  def getField(row: Int, col: Int): FieldInterface = grid(row)(col)

  def replaceField(row: Int, col: Int, field: FieldInterface): Matrix[Field] =
    copy(grid.updated(row, grid(row).updated(col, field)))
}
