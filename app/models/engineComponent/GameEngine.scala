package stratego.model.engineComponent

import stratego.gameEngine.GameStatus._
import GameState._
import stratego.model.gridComponent.Figure.{Bomb, Captain, Colonel, Flag, Lieutenant, Major, Marshal, Miner, Scout, Sergeant, Spy, General}
import stratego.model.gridComponent.FigureType._
import stratego.model.gridComponent.FieldType._
import stratego.model.gridComponent.{Figure, FigureSet, Grid, GridInterface, Position}
import stratego.model.playerComponent.Player

// TODO: Think about adding State Pattern instead of enum gameState
case class GameEngine (grid: GridInterface = Grid().createNewGrid(),
                           gameState: GameState = NOT_STARTED,
                           playerA: Player = Player("PlayerA", A_SIDE),
                           playerB: Player = Player("PlayerB", B_SIDE),
                           activePlayer: Player = Player("PlayerA", A_SIDE),
                           winner: Option[Player] = None,
                           figureSet: Map[Player, FigureSet] = Map(Player("PlayerA", A_SIDE) -> new FigureSet(Player("PlayerA", A_SIDE)), Player("PlayerB", B_SIDE) -> new FigureSet(Player("PlayerB", B_SIDE))),
                           statusLine: GameStatus = IDLE) extends GameEngineInterface {

  def startNewGame(): GameEngineInterface = {
    val newGameEngine = copy(gameState = NEW_GAME)
    publish(GameStartedEvent(newGameEngine)) // Add values which changed to the event so listeners can operate on them
    newGameEngine
  }

  def quitGame(): GameEngineInterface = { //TODO: Review quit game logic
    val newGameEngine = copy(gameState = END)
    publish(GameQuitEvent(newGameEngine))
    newGameEngine
  }

  def setFigure(figureType: FigureType, position: Position): GameEngineInterface = {
    var newGrid = grid
    var newGameState = SET_FIGURES
    var newStatusLine = FIGURE_SET
    var newFigureSet = figureSet
    var nextPlayer = activePlayer
    val inactivePlayer = if (activePlayer == playerA) playerB else playerA

    if (newFigureSet(activePlayer).getFigureCount(figureType) > 0) {
      if (grid.getField(position).getFieldType() == activePlayer.fieldType) {
        if (grid.getField(position).getFigure().isDefined) {
          val figureSetTmp = newFigureSet(activePlayer).addFigure(grid.getField(position).getFigure().get)
          newFigureSet = newFigureSet.updated(activePlayer, figureSetTmp)
        }
        val figureSetTmp = newFigureSet(activePlayer).removeFigure(figureType)
        newFigureSet = newFigureSet.updated(activePlayer, figureSetTmp)
        newGrid = grid.assignField(position, newFigureSet(activePlayer).getLastFigure())
      } else {
        newStatusLine = INVALID_POSITION
      }
    } else {
      // No figures of this type left. Player has to redo this move!
      newStatusLine = NO_FIGURES_LEFT
    }
    if (newFigureSet(inactivePlayer).noFiguresLeft() && newFigureSet(activePlayer).noFiguresLeft()) {
      // No player has figures left to set
      newStatusLine = NO_FIGURES_LEFT
      newGameState = FIGHT
    }
    if (newFigureSet(activePlayer).noFiguresLeft()) {
      // Next player is allowed to set its figures
      nextPlayer = inactivePlayer
    }
    val newGameEngine = copy(grid = newGrid, gameState = newGameState, statusLine = newStatusLine, figureSet = newFigureSet, activePlayer = nextPlayer)
    publish(FigureSetEvent(newGameEngine))
    newGameEngine
  }

  //TODO: remove this method
  def createFigure(figureType: FigureType, player: Player): Figure = {
     figureType match { // Throws an exception if string does not match any FigureType!
      case SCOUT => Scout(player)
      case MARSHAL => Marshal(player)
      case COLONEL => Colonel(player)
      case MAJOR => Major(player)
      case CAPTAIN => Captain(player)
      case LIEUTENANT => Lieutenant(player)
      case SERGEANT => Sergeant(player)
      case MINER => Miner(player)
      case FLAG => Flag(player)
      case SPY => Spy(player)
      case BOMB => Bomb(player)
    }
  }

  def moveFigure(from: Position, to: Position): GameEngineInterface = {
    val source = grid.getField(from)
    val destination = grid.getField(to)
    var newGrid = grid
    var newGameState = gameState
    var newStatusLine = statusLine
    var newWinner = winner
    var nextPlayer = if (activePlayer == playerA) playerB else playerA

    if (source.getFigure.isDefined &&
      source.getFigure.get.player == activePlayer &&
      !isImmobileFigure(source.getFigure.get) ) {
      //TODO: Implement move validation for scout
      val figure = source.getFigure.get
      if (destination.getFieldType != NO_FIELD && destination.getFigure.isEmpty && isValidMove(from, to)) {
        newGrid = newGrid.move(from, to)
        newStatusLine = MOVE_FIGURE
        val newGameEngine = copy(grid = newGrid, gameState = newGameState, statusLine = newStatusLine, winner = newWinner, activePlayer = nextPlayer)
        publish(MoveFigureEvent(newGameEngine))
      } else if (destination.getFigure.isDefined && destination.getFigure.get.player != activePlayer && isValidMove(from, to)) {
        val opponent = destination.getFigure.get
        // TODO: Wrap case code in generic function and stay DRY
        (figure, opponent) match {
          case (a:Spy, b:Marshal) =>
            newGrid = newGrid.move(from, to)
            newStatusLine = SPY_ATTACKS_MARSHAL
            val newGameEngine = copy(grid = newGrid, gameState = newGameState, statusLine = newStatusLine, winner = newWinner, activePlayer = nextPlayer)
            publish(AttackEvent(newGameEngine))
          case (c:Miner, d:Bomb) =>
            newGrid = newGrid.move(from, to)
            newStatusLine = MINER_ATTACKS_BOMB
            val newGameEngine = copy(grid = newGrid, gameState = newGameState, statusLine = newStatusLine, winner = newWinner, activePlayer = nextPlayer)
            publish(AttackEvent(newGameEngine))
          case (a:Figure, b:Bomb) =>
            newGrid = newGrid.assignField(from, None)
            newStatusLine = BOMB_EXPLODES
            val newGameEngine = copy(grid = newGrid, gameState = newGameState, statusLine = newStatusLine, winner = newWinner, activePlayer = nextPlayer)
            publish(AttackEvent(newGameEngine))
          case (a:Figure, b:Flag) =>
            newWinner = Some(activePlayer)
            newGameState = END
            newStatusLine = FLAG_FOUND
            val newGameEngine = copy(grid = newGrid, gameState = newGameState, statusLine = newStatusLine, winner = newWinner, activePlayer = nextPlayer)
            publish(WinnerEvent(newGameEngine))
          case _ =>
            if (figure.strength > opponent.strength) {
              // figure wins!
              newStatusLine = ATTACK_WIN
              newGrid = newGrid.assignField(to, None)
            } else if (figure.strength < opponent.strength) {
              // opponent wins!
              newStatusLine = ATTACK_LOOSE
              newGrid = newGrid.assignField(from, None)
            } else {
              // draw
              newStatusLine = ATTACK_DRAW
              newGrid = newGrid.assignField(from, None)
              newGrid = newGrid.assignField(to, None)
            }
            val newGameEngine = copy(grid = newGrid, gameState = newGameState, statusLine = newStatusLine, winner = newWinner, activePlayer = nextPlayer)
            publish(AttackEvent(newGameEngine))
        }
      } else {
        newStatusLine = INVALID_POSITION
        nextPlayer = activePlayer
        val newGameEngine = copy(grid = newGrid, gameState = newGameState, statusLine = newStatusLine, winner = newWinner, activePlayer = nextPlayer)
        publish(InvalidMoveEvent(newGameEngine))
      }
    } else {
      newStatusLine = NO_VALID_FIGURE
      nextPlayer = activePlayer
      val newGameEngine = copy(grid = newGrid, gameState = newGameState, statusLine = newStatusLine, winner = newWinner, activePlayer = nextPlayer)
      publish(InvalidMoveEvent(newGameEngine))
    }
    copy(grid = newGrid, gameState = newGameState, statusLine = newStatusLine, winner = newWinner, activePlayer = nextPlayer)
  }

  private def isImmobileFigure(figure: Figure): Boolean = {
    figure match {
      case b: Bomb => true
      case f: Flag => true
      case _ => false
    }
  }

  private def isValidMove(from: Position, to: Position): Boolean = {
    ((to.row == from.row + 1 || to.row == from.row - 1) && from.col == to.col) || ((to.col == from.col + 1 || to.col == from.col - 1) && from.row == to.row)
  }

  def startBattle: GameEngineInterface = {
    val newGameEngine = copy(gameState = FIGHT)
    publish(GameStartedEvent(newGameEngine))
    newGameEngine
  }

  def changePlayer: GameEngineInterface = {
    val newGameEngine = copy(activePlayer = if (activePlayer == playerA) playerB else playerA)
    publish(ChangePlayerEvent(newGameEngine))
    newGameEngine
  }
  //does not work yet !!!
  def setUpDefaultGrid: GameEngineInterface = {
    var newGrid = Grid().createNewGrid()
    val tempFigureSetA = createFigureList(playerA)
    val tempFigureSetB = createFigureList(playerB)
    for {
      row <- 0 until 4
      col <- 0 until 10
      figure <- tempFigureSetA
    } newGrid = newGrid.assignField(Position(row, col), Some(figure))
    for {
      row <- 6 until 10
      col <- 0 until 10
      figure <- tempFigureSetB
    } newGrid = newGrid.assignField(Position(row, col), Some(figure))
    val newGameEngine = copy(grid = newGrid, gameState = FIGHT)
    publish(FigureSetEvent(newGameEngine))
    newGameEngine
  }

  def createFigureList(player: Player): List[Figure] = {
    List(Bomb(player), Bomb(player), Bomb(player), Bomb(player), Bomb(player), Bomb(player),
      Marshal(player),
      General(player),
      Colonel(player), Colonel(player),
      Major(player), Major(player), Major(player),
      Captain(player), Captain(player), Captain(player), Captain(player),
      Lieutenant(player), Lieutenant(player), Lieutenant(player), Lieutenant(player),
      Sergeant(player), Sergeant(player), Sergeant(player), Sergeant(player),
      Miner(player), Miner(player), Miner(player), Miner(player), Miner(player),
      Scout(player), Scout(player), Scout(player), Scout(player), Scout(player), Scout(player), Scout(player), Scout(player),
      Flag(player),
      Spy(player))
  }

  def gridToString: String = grid.toStringTUI(gameState, activePlayer) //TODO: When time refactor these toStringTUI methods

  def getFigure(position: Position): Option[Figure] = grid.getField(position).getFigure

  def getFieldStringGUI(position:Position): String = grid.getField(position).toStringGUI(gameState, activePlayer)

  def getFigureSetActivePlayer: FigureSet = figureSet(activePlayer)

  def getGrid: GridInterface = grid

  def getGameState: GameState = gameState

  def getActivePlayer: Player = activePlayer

  def getWinner: Option[Player] = winner

  def getStatusLine: GameStatus = statusLine

}
