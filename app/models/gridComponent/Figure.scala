package stratego.model.gridComponent

import stratego.model.playerComponent.Player

sealed abstract class Figure(val figureType: FigureType.FigureType, val stringOut: String, val strength: Int, val player: Player) {
  override def toString: String = stringOut
}

object Figure {

  case class Scout(override val player: Player) extends Figure(FigureType.SCOUT, "[02]",  2, player)
  case class Marshal(override val player: Player) extends Figure(FigureType.MARSHAL, "[10]",  10, player)
  case class General(override val player: Player) extends Figure(FigureType.GENERAL, "[09]",  9, player)
  case class Colonel(override val player: Player) extends Figure(FigureType.COLONEL, "[08]",  8, player)
  case class Major(override val player: Player) extends Figure(FigureType.MAJOR, "[07]",  7, player)
  case class Captain(override val player: Player) extends Figure(FigureType.CAPTAIN, "[06]",  6, player)
  case class Lieutenant(override val player: Player) extends Figure(FigureType.LIEUTENANT, "[05]",  5, player)
  case class Sergeant(override val player: Player) extends Figure(FigureType.SERGEANT, "[04]",  4, player)
  case class Miner(override val player: Player) extends Figure(FigureType.MINER, "[M3]",  3, player)
  case class Flag(override val player: Player) extends Figure(FigureType.FLAG, "[FL]",  0, player)
  case class Spy(override val player: Player) extends Figure(FigureType.SPY, "[SP]",  1, player)
  case class Bomb(override val player: Player) extends Figure(FigureType.BOMB, "[BO]",  100, player)

}