package stratego.model.engineComponent

object GameState extends Enumeration {

  type GameState = Value
  val NEW_GAME = Value("New Game created")
  val NOT_STARTED = Value("Game not started")
  val SET_FIGURES = Value("Set Figures")
  val FIGHT = Value("Fight")
  val END = Value("End of the game")
}
