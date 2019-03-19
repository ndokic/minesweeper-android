package ndokic.minesweeper.game

class Game(val col:Int, val row:Int, val numOfMines : Int) {
    val fields: ArrayList<GameField> = ArrayList()
    val size = row * col
    var gameState = GameState.UNINITIAZED
}