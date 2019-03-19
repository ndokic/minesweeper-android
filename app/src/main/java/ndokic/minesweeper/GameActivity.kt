package ndokic.minesweeper

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import ndokic.minesweeper.game.Game
import ndokic.minesweeper.game.GameField

class GameActivity : AppCompatActivity() {
    var rows : Int = 0
    var cols : Int = 0
    var mines : Int = 0

    lateinit var game :Game //Game(cols, rows, 10)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        rows = intent.getIntExtra(TAG_GAME_ROWS, 9)
        cols = intent.getIntExtra(TAG_GAME_COLS, 9)
        mines = intent.getIntExtra(TAG_GAME_MINES, 10)
        game = Game(cols, rows, mines)
        createGameLayout()
    }

    fun createGameLayout() {
        val inflater = LayoutInflater.from(this)
        val rootLayout = findViewById<LinearLayout>(R.id.layout_root)
        for (i in 1..rows) {
            val row = LinearLayout(this)
            row.orientation = LinearLayout.HORIZONTAL
            row.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            rootLayout.addView(row)

            for (j in 1..cols) {
                val button =  inflater.inflate(R.layout.game_button, null) as Button
                val field = GameField(game, game.fields.size, cols, rows, button)
                button.setOnClickListener {game.fieldClick(field)}
                button.setOnLongClickListener { game.fieldMarkedMine(field) }
                val lp =  LinearLayout.LayoutParams(60, 60)
                lp.setMargins(2,2,2,2)
                button.layoutParams =lp
                row.addView(button)
                game.fields.add(field)
            }
        }

     //   game.initGame(5)

    }
}