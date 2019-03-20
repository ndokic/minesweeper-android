package ndokic.minesweeper.game

import android.os.Handler
import android.util.Log
import ndokic.minesweeper.R
import android.support.v4.os.HandlerCompat.postDelayed
import kotlin.random.Random


class Game(val col:Int, val row:Int, val numOfMines : Int, val gameChangeListener: GameChangeListener) {
    val fields: ArrayList<GameField> = ArrayList()
    val size = row * col
    var gameState = GameState.UNINITIAZED
    var minesPlaced = 0
    var minesFound = 0
    var moves = 0
    var revealedFields = 0
    var fieldsToReveal = size - numOfMines


    fun fieldMarkedMine(field: GameField) : Boolean{ //called on long press
        moves ++
        field.button.setText("*")
        field.state = FieldState.MINE
        gameChangeListener.minesLeftChange(numOfMines - ++minesFound)
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
        moves++
        field.button.setText("");
        field.state = FieldState.UNREVEALED
        gameChangeListener.minesLeftChange(numOfMines - --minesFound)

    }

    fun checkField(field : GameField) {
        moves++

        if(field.state == FieldState.MINE) {
            clearMine(field)
            return
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
            field.button.setBackgroundResource(R.color.colorPrimaryDark) //reset hint color when you click on it
            revealField(field)
        }
        else {
            field.button.setBackgroundResource(R.color.colorButtonOpen)
            revealField(field)
            field.neighbors.forEach { openField(fields[it]) }
        }
    }

    fun revealField(field : GameField) {
        field.button.isEnabled = false;
        field.state = FieldState.REVEALED
        if( ++revealedFields == fieldsToReveal)
            gameWon()
    }

    fun hint() {
        if(gameState != GameState.STARTED) return
        var ran = Random.nextInt(0, size)
        if(fields[ran].state == FieldState.UNREVEALED && !fields[ran].hasMine) {
            fields[ran].button.setBackgroundResource(R.color.colorPrimary)
            startTime -= 10000  //Add 10 second time penalty for using hint
        } else {
            hint()
        }
    }
    fun gameWon() {
        gameState = GameState.WON
        fields.forEach { if(it.hasMine) {
            it.button.setText("*")
            it.button.setBackgroundResource(R.color.colorPrimary)
        } }
        gameChangeListener.onGameWon()
    }

    fun mineClicked(field: GameField) {
        fields.forEach { if(it.hasMine) {
            it.button.setText("*")
            it.button.setBackgroundResource(R.color.colorAccent)
        } }

        gameState = GameState.BUSTED
        gameChangeListener.onBusted()
    }
    fun initGame(clickedIndex: Int) {
        initMines(clickedIndex)
        initFields()

        gameChangeListener.minesLeftChange(numOfMines)
        startTime = System.currentTimeMillis()
        timerHandler.post(timer)

        /*fields.forEach {
            if (it.hasMine) it.button.setText("*") else if(it.neighborsWithMines >0)it.button.setText(""+it.neighborsWithMines)
        }*/
    }

    fun initMines(clickedIndex : Int ) { //We do not want a mine on field that is clicked first
        val chanceForMine = 1 / (size.toDouble()/numOfMines.toDouble())
        for(i in 0..size-1) {
            if(i != clickedIndex)  {
                val rand = Math.random()
                if(rand < chanceForMine && !fields[i].hasMine) {
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
    var startTime : Long = 0;
    val timerHandler = Handler();
    val timer : Runnable = object : Runnable{
        override fun run() {
            val millis = System.currentTimeMillis() - startTime
            var seconds = (millis / 1000)
            val minutes = seconds / 60
            seconds = seconds.rem( 60);
            gameChangeListener.updateTime(String.format("%d:%02d", minutes, seconds))
            if(gameState == GameState.STARTED) //stop when game state changes
                    timerHandler.postDelayed(this, 500)
        }
    }


    interface GameChangeListener {
        fun onGameWon()
        fun onBusted()
        fun minesLeftChange(minesLeft : Int)
        fun updateTime(time : String)
    }
}