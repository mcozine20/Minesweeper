package com.example.minesweeper

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.minesweeper.models.MinesweeperModel
import kotlinx.android.synthetic.main.activity_main.*
import android.support.design.widget.Snackbar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MinesweeperModel.initializeModel()
        MinesweeperModel.setBoardSize(8)
        MinesweeperModel.resetModel()
        initializeButtons()
    }

    private fun initializeButtons() {
        initializeFlagButton()
        initializeRestartButton()
        initializeSmallButton()
        initializeMediumButton()
        initializeLargeButton()
    }

    private fun initializeFlagButton() {
        btnFlag.setOnClickListener {
            MinesweeperModel.setPlaceFlagMode()
            Snackbar.make(minesweeperView, getString(R.string.place_flag_msg), Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun initializeRestartButton() {
        btnRestart.setOnClickListener {
            minesweeperView.resetGame()
        }
    }

    private fun initializeSmallButton() {
        btnSmall.setOnClickListener {
            MinesweeperModel.setBoardSize(5)
            minesweeperView.resetGame()
        }
    }

    private fun initializeMediumButton() {
        btnMedium.setOnClickListener {
            MinesweeperModel.setBoardSize(8)
            minesweeperView.resetGame()
        }
    }

    private fun initializeLargeButton() {
        btnLarge.setOnClickListener {
            MinesweeperModel.setBoardSize(10)
            minesweeperView.resetGame()
        }
    }

}
