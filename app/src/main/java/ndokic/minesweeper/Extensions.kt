package ndokic.minesweeper

import android.support.annotation.IdRes
import android.support.v7.app.AppCompatActivity
import android.view.View

const val TAG_GAME_ROWS = "tag_game_rows"
const val TAG_GAME_COLS = "tag_game_cols"
const val TAG_GAME_MINES = "tag_game_mines"

fun <T : View> AppCompatActivity.bind(@IdRes res : Int) : Lazy<T> {
    @Suppress("UNCHECKED_CAST")
    return lazy { findViewById(res) as T }
}