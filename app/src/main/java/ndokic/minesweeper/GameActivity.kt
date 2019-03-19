package ndokic.minesweeper

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout

class GameActivity : AppCompatActivity() {
    val rows = 9
    val cols = 9

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
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

                button.setOnClickListener { (it as Button).setText("C")}
                row.addView(button)

            }
        }
    }
}