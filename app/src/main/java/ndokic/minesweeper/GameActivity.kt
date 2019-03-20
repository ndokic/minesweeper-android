package ndokic.minesweeper

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import ndokic.minesweeper.game.Game
import ndokic.minesweeper.game.GameField

class GameActivity : AppCompatActivity(), Game.GameChangeListener  {


    var rows : Int = 0
    var cols : Int = 0
    var mines : Int = 0

    lateinit var game :Game //Game(cols, rows, 10)
    private val textTime: TextView by bind(R.id.text_time)
    private val textMines: TextView by bind(R.id.text_mines)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        rows = intent.getIntExtra(TAG_GAME_ROWS, 9)
        cols = intent.getIntExtra(TAG_GAME_COLS, 9)
        mines = intent.getIntExtra(TAG_GAME_MINES, 10)
        game = Game(cols, rows, mines, this)
        createGameLayout()
    }

    fun createGameLayout() {
        val inflater = LayoutInflater.from(this)
        val rootLayout = findViewById<LinearLayout>(R.id.layout_root)
        rootLayout.removeAllViews()
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
    }

    fun playAgain() {
        game = Game(cols, rows, mines, this)
        createGameLayout()
        textMines.setText(mines.toString())
        textTime.setText("0:00")
    }
    
    override fun onGameWon() {
        AlertDialog.Builder(this).setTitle(R.string.text_dialog_won_title).setMessage(resources.getString(R.string.text_dialog_won_message, textTime.text, game.moves))
            .setPositiveButton(R.string.text_play_again,  { dialog, i -> playAgain() })
            .setNegativeButton(R.string.text_go_back, {dialog, i-> finish()} ).show()
    }

    override fun onBusted() {
        AlertDialog.Builder(this).setTitle(R.string.text_dialog_lost_title).setMessage(R.string.text_dialog_lost_message)
            .setPositiveButton(R.string.text_play_again,  { dialog, i -> playAgain() })
            .setNegativeButton(R.string.text_go_back, {dialog, i-> finish()} ).show()
    }

    override fun minesLeftChange(minesLeft: Int) {
        textMines.setText(minesLeft.toString())
    }
    override fun updateTime(time : String) {
        textTime.setText(time)
    }

}