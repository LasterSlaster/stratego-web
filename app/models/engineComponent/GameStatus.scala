package stratego.gameEngine

object GameStatus extends Enumeration {
    type GameStatus = Value
    val WRONG_INPUT = Value("Invalid Input")
    val NO_FIGURES_LEFT = Value("Not enough figures of this type")
    val INVALID_POSITION = Value("Figure can't be placed on this field")
    val FIGURE_SET = Value("Figure was successfully placed on field")
    val IDLE = Value("Idle")
    val NO_VALID_FIGURE = Value("No valid figure found")
    val PLAYERA_WINS = Value("Player A wins")
    val PLAYERB_WINS = Value("Player B wins")
    val MOVE_FIGURE = Value("Moved figure")
    val ATTACK_WIN = Value("Attack was won")
    val ATTACK_LOOSE = Value("Attack was lost")
    val ATTACK_DRAW = Value("Attack was a draw")
    val SPY_ATTACKS_MARSHAL = Value("Spy encounters marshal!")
    val MINER_ATTACKS_BOMB = Value("Miner defused a bomb!")
    val BOMB_EXPLODES = Value("Bomb!")
    val FLAG_FOUND = Value("Flag was found")
}
