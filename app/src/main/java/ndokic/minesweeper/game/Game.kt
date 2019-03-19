package ndokic.minesweeper.game

import android.util.Log

class Game(val col:Int, val row:Int, val numOfMines : Int) {
    val fields: ArrayList<GameField> = ArrayList()
    val size = row * col
    var gameState = GameState.UNINITIAZED
    var minesPlaced = 0

    fun fieldClick(field : GameField) {
        field.neighbors.forEach {
            fields[it].button.setText("A") }
    }

    fun initGame(clickedIndex: Int) {
        initMines(clickedIndex)

        fields.forEach {
            if (it.hasMine) it.button.setText("*")
        }
    }

    fun initMines(clickedIndex : Int ) { //We do not want a mine on field that is clicked first
        val chanceForMine = 1 / (size.toDouble()/numOfMines.toDouble())
        Log.d("MINES", "ChanceForMine: " + chanceForMine)
        for(i in 0..size-1) {
            if(i != clickedIndex)  {
                val rand = Math.random()
                Log.d("MINES", "Random: " + rand)
                if(rand < chanceForMine && !fields[i].hasMine) {
                    Log.d("MINES", "Placing mine: " + i)
                    fields[i].hasMine = true
                    if( ++minesPlaced == numOfMines) return
                }
            }
        }
        if(minesPlaced < numOfMines) initMines(clickedIndex)
    }
}