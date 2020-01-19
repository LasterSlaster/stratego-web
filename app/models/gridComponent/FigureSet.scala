package stratego.model.gridComponent

import stratego.model.gridComponent.Figure._
import stratego.model.playerComponent.Player

case class FigureSet(figures: Map[FigureType.FigureType, List[Figure]], lastFigure: Option[Figure]) {
 def this(player: Player) = this(Map(
   FigureType.BOMB -> List( Bomb(player), Bomb(player), Bomb(player), Bomb(player), Bomb(player), Bomb(player)),
   FigureType.MARSHAL -> List(Marshal(player)),
   FigureType.GENERAL -> List(General(player)),
   FigureType.COLONEL -> List(Colonel(player), Colonel(player)),
   FigureType.MAJOR -> List(Major(player), Major(player), Major(player)),
   FigureType.CAPTAIN -> List(Captain(player), Captain(player), Captain(player), Captain(player)),
   FigureType.LIEUTENANT -> List(Lieutenant(player), Lieutenant(player), Lieutenant(player), Lieutenant(player)),
   FigureType.SERGEANT -> List(Sergeant(player), Sergeant(player), Sergeant(player), Sergeant(player)),
   FigureType.MINER -> List(Miner(player), Miner(player), Miner(player), Miner(player), Miner(player)),
   FigureType.SCOUT -> List(Scout(player), Scout(player), Scout(player), Scout(player), Scout(player), Scout(player), Scout(player), Scout(player)),
   FigureType.SPY -> List(Spy(player)),
   FigureType.FLAG -> List(Flag(player))
 ), None)

  def removeFigure(figureType: FigureType.FigureType): FigureSet = {
    var figureList = figures.get(figureType).get
    val lastFigure = figureList.head
    figureList = figureList diff List(lastFigure)
    copy(figures.updated(figureType, figureList), Some(lastFigure))
  }

  def addFigure(figure: Figure): FigureSet = {
    val figureList = figure :: figures.get(figure.figureType).get
    copy(figures.updated(figure.figureType, figureList), Some(figure))
  }

  def getFigureCount(figureType: FigureType.FigureType): Int = figures.get(figureType).get.size

  def getLastFigure(): Option[Figure] = lastFigure

  def noFiguresLeft(): Boolean = figures.values.foldLeft(0)((prev, next) => prev + next.size) == 0

}
