package com.example.minesweeper.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.design.widget.Snackbar
import android.util.AttributeSet
import android.view.View
import com.example.minesweeper.models.MinesweeperModel
import android.view.MotionEvent
import com.example.minesweeper.R
import com.example.minesweeper.data.Tile

class MinesweeperView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var gameOver: Boolean = false

    private val paintLine: Paint = Paint()
    private val paintBomb: Paint = Paint()
    private val paintExplosion: Paint = Paint()
    private val paintFlag: Paint = Paint()
    private val paintNum: Paint = Paint()
    private val paintHidden: Paint = Paint()

    private val win: String = "WIN"
    private val continueGame: String = "CONTINUE"
    private val selectedBomb: String = "SELECTED_BOMB"
    private val flagWrong: String = "FLAG_WRONG"

    init {
        paintLine.color = Color.rgb(80,80,80)
        paintLine.style = Paint.Style.STROKE
        paintLine.strokeWidth = 2f

        paintBomb.color = Color.BLACK
        paintBomb.style = Paint.Style.FILL

        paintExplosion.color = Color.RED
        paintExplosion.style = Paint.Style.FILL

        paintFlag.color = Color.RED
        paintFlag.style = Paint.Style.FILL

        paintNum.color = Color.BLUE
        paintNum.textSize = (600 / MinesweeperModel.getBoardSize()).toFloat()
        paintNum.strokeWidth = 20f

        paintHidden.color = Color.GRAY
        paintHidden.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas?) {
        drawGameBoard(canvas)
        drawAllTiles(canvas)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!gameOver) {
            if (event?.action == MotionEvent.ACTION_DOWN) {
                val tX = event.x.toInt() / (width / MinesweeperModel.getBoardSize())
                val tY = event.y.toInt() / (height / MinesweeperModel.getBoardSize())
                if (tX < MinesweeperModel.getBoardSize() && tY < MinesweeperModel.getBoardSize()
                    && !MinesweeperModel.getBoardContent(tX, tY).wasClicked) {
                    val status: String = MinesweeperModel.clickOnTile(tX, tY)
                    checkStatus(status)
                }
            }
            invalidate()
        }
        return true
    }

    private fun drawGameBoard(canvas: Canvas?) {
        for (i in 0 until MinesweeperModel.getBoardSize()) {
            canvas?.drawLine(0f, ((i + 1) * height / MinesweeperModel.getBoardSize()).toFloat(),
                width.toFloat(), ((i + 1) * height / MinesweeperModel.getBoardSize()).toFloat(), paintLine)
        }
        for (i in 0 until MinesweeperModel.getBoardSize()) {
            canvas?.drawLine(((i + 1) * (width / MinesweeperModel.getBoardSize())).toFloat(), 0f,
                ((i + 1) * (width / MinesweeperModel.getBoardSize())).toFloat(), height.toFloat(), paintLine)
        }
    }

    private fun drawAllTiles(canvas: Canvas?) {
        for (i in 0 until MinesweeperModel.getBoardSize()) {
            for (j in 0 until MinesweeperModel.getBoardSize()) {
                drawOneTile(canvas, MinesweeperModel.getBoardContent(i, j), i, j)
            }
        }
    }

    private fun drawOneTile(canvas: Canvas?, tile: Tile, x: Int, y: Int) {
        if (tile.type == 1 && tile.wasClicked) {
            drawExplodedBomb(canvas, x, y)
        } else if (tile.isFlagged) {
            drawFlag(canvas, x, y)
        } else if (!tile.revealed) {
            drawHidden(canvas, x, y)
        } else if (tile.type == 2) {
            drawNum(canvas, x, y)
        } else if (tile.type == 1) {
            drawBomb(canvas, x, y)
        }

    }

    private fun drawBomb(canvas: Canvas?, x: Int, y: Int) {
        val centerX = (x * width/MinesweeperModel.getBoardSize() + width/(2 * MinesweeperModel.getBoardSize())).toFloat()
        val centerY = (y * height/MinesweeperModel.getBoardSize() + height/(2 * MinesweeperModel.getBoardSize())).toFloat()
        val radius = height/(2.5 * MinesweeperModel.getBoardSize())
        canvas?.drawCircle(centerX, centerY, radius.toFloat(), paintBomb)
    }

    private fun drawExplodedBomb(canvas: Canvas?, x: Int, y: Int) {
        canvas?.drawRect(
            (x * width / MinesweeperModel.getBoardSize()).toFloat(),
            (y * height / MinesweeperModel.getBoardSize()).toFloat(),
            ((x+1) * width / MinesweeperModel.getBoardSize()).toFloat(),
            ((y+1) * height / MinesweeperModel.getBoardSize()).toFloat(),
            paintExplosion)
        drawBomb(canvas, x, y)
    }

    private fun drawFlag(canvas: Canvas?, x: Int, y: Int) {
        val centerX = (x * width/MinesweeperModel.getBoardSize() + width/(2 * MinesweeperModel.getBoardSize())).toFloat()
        val centerY = (y * height/MinesweeperModel.getBoardSize() + height/(2 * MinesweeperModel.getBoardSize())).toFloat()
        val radius = height/(2.5 * MinesweeperModel.getBoardSize())
        canvas?.drawCircle(centerX, centerY, radius.toFloat(), paintFlag)
    }

    private fun drawNum(canvas: Canvas?, x: Int, y: Int) {
        val numToDraw = MinesweeperModel.getBoardContent(x, y).minesAround
        if (numToDraw == 0) return
        canvas?.drawText(
            numToDraw.toString(),
            ((x * width / MinesweeperModel.getBoardSize()) + width / (2.5 * MinesweeperModel.getBoardSize())).toFloat(),
            ((y * height / MinesweeperModel.getBoardSize()) + height / (1.5 * MinesweeperModel.getBoardSize())).toFloat(),
            paintNum)
    }

    private fun drawHidden(canvas: Canvas?, x: Int, y: Int) {
        canvas?.drawRect(
            (x * width / MinesweeperModel.getBoardSize() + width / (10 * MinesweeperModel.getBoardSize())).toFloat(),
            (y * height / MinesweeperModel.getBoardSize() + height / (10 * MinesweeperModel.getBoardSize())).toFloat(),
            ((x+1) * width / MinesweeperModel.getBoardSize() - width / (10 * MinesweeperModel.getBoardSize())).toFloat(),
            ((y+1) * height / MinesweeperModel.getBoardSize() - height / (10 * MinesweeperModel.getBoardSize())).toFloat(),
            paintHidden)
    }

    private fun checkStatus(status: String) {
        if (status == continueGame) {
            return
        } else if (status == win) {
            Snackbar.make(this, context.getString(R.string.win_msg), Snackbar.LENGTH_LONG).show()
            gameOver = true
        } else if (status == flagWrong) {
            Snackbar.make(this, context.getString(R.string.wrong_flag_msg), Snackbar.LENGTH_LONG).show()
            gameOver = true
        } else if (status == selectedBomb) {
            Snackbar.make(this, context.getString(R.string.hit_bomb_msg), Snackbar.LENGTH_LONG).show()
            gameOver = true
        }
    }

    fun resetGame() {
        MinesweeperModel.resetModel()
        gameOver = false
        invalidate()
    }
}