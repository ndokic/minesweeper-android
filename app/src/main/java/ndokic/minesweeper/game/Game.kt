package ndokic.minesweeper.game

import android.util.Log
import ndokic.minesweeper.R

class Game(val col:Int, val row:Int, val numOfMines : Int) {
    val fields: ArrayList<GameField> = ArrayList()
    val size = row * col
    var gameState = GameState.UNINITIAZED
    var minesPlaced = 0


    fun fieldMarkedMine(field: GameField) : Boolean{ //called on long press

        field.button.setText("*")
        field.state = FieldState.MINE
        return true
    }

    fun fieldClick(field : GameField) {

        if(gameState == GameState.UNINITIAZED) {
            initGame(field.index)
            gameState = GameState.STARTED
            fieldClick(field)
        }
        if(gameState == GameState.STARTED) {
            checkField(field)
        }
    }

    fun clearMine(field: GameField) {
        field.button.setText("");
        field.state = FieldState.UNREVEALED
    }

    fun checkField(field : GameField) {
        if(field.state == FieldState.MINE) {
            clearMine(field)
        }

        if(field.hasMine) {
            mineClicked(field)
        } else {
            openField(field)
        }
    }

    fun openField(field : GameField) {
        if (field.hasMine || field.state == FieldState.REVEALED) return
        if(field.neighborsWithMines >0)
        {
            field.button.setText(field.neighborsWithMines.toString())
            field.button.isEnabled = false;
            field.state = FieldState.REVEALED
        }
        else {
            field.button.setBackgroundResource(R.color.colorButtonOpen)
            field.button.isEnabled = false;
            field.state = FieldState.REVEALED
            field.neighbors.forEach { openField(fields[it]) }
        }
    }

    fun mineClicked(field: GameField) {
        fields.forEach { if(it.hasMine) {
            it.button.setText("*")
            it.button.setBackgroundResource(R.color.colorAccent)
        } }

        gameState = GameState.BUSTED
         //TODO game over
    }
    fun initGame(clickedIndex: Int) {
        initMines(clickedIndex)
        initFields()

        /*fields.forEach {
            if (it.hasMine) it.button.setText("*") else if(it.neighborsWithMines >0)it.button.setText(""+it.neighborsWithMines)
        }*/
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
    fun initFields() {
        fields.forEach {
            if (!it.hasMine) {
                var neighborsWithMines = 0;
                it.neighbors.forEach {
                    if (fields[it].hasMine) neighborsWithMines++
                }
                it.neighborsWithMines = neighborsWithMines
            }
        }
    }
}