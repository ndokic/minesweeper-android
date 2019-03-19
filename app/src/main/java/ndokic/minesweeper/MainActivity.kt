package ndokic.minesweeper

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private val seekbarRow: SeekBar by bind(R.id.seekBarRow)
    private val seekbarCol: SeekBar by bind(R.id.seekBarCol)
    private val seekbarMines: SeekBar by bind(R.id.seekBarMines)
    private val textRow: TextView by bind(R.id.textViewRow)
    private val textCol: TextView by bind(R.id.textViewCol)
    private val textMines: TextView by bind(R.id.textViewMines)

    private var customRows = 10;
    private var customCols = 10;
    private var customMines = 10;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekbarRow.setOnSeekBarChangeListener(rowChangeListener)
        seekbarCol.setOnSeekBarChangeListener(colChangeListener)
        seekbarMines.setOnSeekBarChangeListener(minesChangeListener)


        textRow.setText(resources.getString(R.string.text_custom_rows, 10)) //Because seekbar.setProgress is not available until api 26
        textCol.setText(resources.getString(R.string.text_custom_cols, 10))
        textMines.setText(resources.getString(R.string.text_custom_mines, 10))
    }

    fun onClickEasy(v : View) {
        startGame(9,9,10)
    }

    fun onClickMedium(v : View) {
        startGame(16,16,40)
    }

    fun onClickHard(v : View) {
        startGame(30,16,99)
    }
    fun onClickCustom(v : View) {
        startGame(customRows,customCols,customMines)
    }

    fun startGame(rows : Int, cols: Int, mines : Int) {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra(TAG_GAME_ROWS, rows)
        intent.putExtra(TAG_GAME_COLS, cols)
        intent.putExtra(TAG_GAME_MINES, mines)
        startActivity(intent)
    }
    var rowChangeListener: SeekBar.OnSeekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {

        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            // updated continuously as the user slides the thumb
            textRow.setText(resources.getString(R.string.text_custom_rows, progress)) //TODO
            customRows = progress
            seekbarMines.max = customCols*customRows - 1
        }
        override fun onStartTrackingTouch(seekBar: SeekBar) {}
        override fun onStopTrackingTouch(seekBar: SeekBar) {}
    }

    var colChangeListener: SeekBar.OnSeekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {

        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            // updated continuously as the user slides the thumb
            textCol.setText(resources.getString(R.string.text_custom_cols, progress))
            customCols = progress;
            seekbarMines.max = customCols*customRows - 1
        }
        override fun onStartTrackingTouch(seekBar: SeekBar) {}
        override fun onStopTrackingTouch(seekBar: SeekBar) {}
    }

    var minesChangeListener: SeekBar.OnSeekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {

        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            // updated continuously as the user slides the thumb
            textMines.setText(resources.getString(R.string.text_custom_mines, progress))
            customMines = progress;
        }
        override fun onStartTrackingTouch(seekBar: SeekBar) {}
        override fun onStopTrackingTouch(seekBar: SeekBar) {}
    }
}
