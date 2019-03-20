package ndokic.minesweeper.game

import android.widget.Button

class GameField(val index: Int, val col : Int, val row : Int, val button : Button) {
    var hasMine = false
    var neighborsWithMines: Int = 0
    var state = FieldState.UNREVEALED

    val neighbors : ArrayList<Int> = ArrayList()

    init {
        findNeighbors()
    }

    fun findNeighbors() {
        neighbors.add(top(left(index)))
        neighbors.add(top(index))
        neighbors.add(top(right(index)))
        neighbors.add(left(index))
        neighbors.add(right(index))
        neighbors.add(bottom(left(index)))
        neighbors.add(bottom(index))
        neighbors.add(bottom(right(index)))

        while(neighbors.contains(-1)) {
            neighbors.remove(-1)
        }
    }

    //Finding neighboring index
    fun left(n : Int) :Int {
        if(n<0) return -1;
        return if ( n.rem(col) != 0) n-1 else -1
    }
    fun right(n : Int) :Int {
        if(n<0) return -1;
        return if ( n.rem(col) != col-1) n+1 else -1
    }
    fun top(n : Int) :Int {
        if(n<0) return -1;
        return if(n-col > 0) n - col else -1
    }
    fun bottom(n : Int) :Int {
        if(n<0) return -1;
        return if ( n+col < col*row) n+col else -1
    }
}