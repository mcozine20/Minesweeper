package com.example.minesweeper.models

import com.example.minesweeper.R
import com.example.minesweeper.data.Tile

object MinesweeperModel {

    private var placeFlagMode: Boolean = false

    private var MAXBOARDSIZE: Int = 12
    private var boardSize: Int = 12
    private var numBombs: Int = (boardSize-1 until boardSize+2).random()

    private const val EMPTY: Int = 0
    private const val BOMB: Int = 1
    private const val NUM: Int = 2
    
    private const val WIN: String = "WIN"
    private const val CONTINUE_GAME: String = "CONTINUE"
    private const val SELECTED_BOMB: String = "SELECTED_BOMB"
    private const val FLAG_WRONG: String = "FLAG_WRONG"

    private val gameBoardMatrix = Array(boardSize) {
        Array(boardSize) {
            Tile(EMPTY)
        }
    }

    fun initializeModel() {
        initializeBombs()
        initializeNumberTiles()
    }

    private fun initializeBombs() {
        var placedBombs = 0
        while (placedBombs < numBombs) {
            val randX = (0 until boardSize).random()
            val randY = (0 until boardSize).random()
            if (gameBoardMatrix[randX][randY].type == EMPTY) {
                gameBoardMatrix[randX][randY].type = BOMB
                placedBombs += 1
            }
        }
    }

    private fun initializeNumberTiles() {
        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                if (gameBoardMatrix[i][j].type != BOMB) {
                    gameBoardMatrix[i][j].type = NUM
                    gameBoardMatrix[i][j].minesAround = findSurroundingBombs(i, j)
                }
            }
        }
    }

    private fun findSurroundingBombs(x: Int, y: Int): Int {
        var surroundingBombCount = 0

        for (i in x-1..x+1) {
            for (j in y-1..y+1) {
                if (i in 0 until boardSize && j in 0 until boardSize) {
                    if (gameBoardMatrix[i][j].type == BOMB) {
                        surroundingBombCount += 1
                    }
                }
            }
        }

        return surroundingBombCount
    }
    
    fun resetModel() {
        for (i in 0 until MAXBOARDSIZE) {
            for (j in 0 until MAXBOARDSIZE) {
                gameBoardMatrix[i][j].type = EMPTY
                gameBoardMatrix[i][j].minesAround = 0
                gameBoardMatrix[i][j].wasClicked = false
                gameBoardMatrix[i][j].isFlagged = false
                gameBoardMatrix[i][j].revealed = false
            }
        }
        placeFlagMode = false
        numBombs = (boardSize-1 until boardSize+2).random()
        initializeModel()
    }

    fun clickOnTile(x: Int, y: Int): String {
        if (placeFlagMode) {
            placeFlagMode = false
            return placeFlag(x, y)
        } else {
            return revealTile(x, y)
        }
    }

    private fun placeFlag(x: Int, y: Int): String {
        if (gameBoardMatrix[x][y].type != BOMB) {
            return playerLoses(FLAG_WRONG)
        } else {
            gameBoardMatrix[x][y].isFlagged = true
            if (checkForWin()) {
                return playerWins()
            }
        }
        return CONTINUE_GAME
    }

    private fun revealTile(x: Int, y: Int): String {
        revealSurroundings(x, y)
        gameBoardMatrix[x][y].wasClicked = true
        gameBoardMatrix[x][y].revealed = true
        if (gameBoardMatrix[x][y].type == BOMB) {
            return playerLoses(SELECTED_BOMB)
        }
        return CONTINUE_GAME
    }

    private fun revealSurroundings(x: Int, y: Int) {
        val current = gameBoardMatrix[x][y]
        if (current.type != BOMB && current.minesAround == 0 && !current.revealed) {
            current.revealed = true
            for (i in x-1..x+1) {
                for (j in y-1..y+1) {
                    if (i in 0 until boardSize && j in 0 until boardSize) {
                        revealSurroundings(i, j)
                        gameBoardMatrix[i][j].wasClicked = true
                        gameBoardMatrix[i][j].revealed = true
                    }
                }
            }
        }
    }

    private fun checkForWin(): Boolean {
        var numFlags = 0

        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                if (gameBoardMatrix[i][j].isFlagged) {
                    numFlags += 1
                    if (numFlags == numBombs) {
                        return true
                    }
                }
            }
        }

        return false
    }

    private fun playerLoses(reason: String): String {
        revealAll()
        return reason
    }

    private fun playerWins(): String {
        revealAll()
        return WIN
    }

    private fun revealAll() {
        for (i in 0 until boardSize) {
            for (j in 0 until boardSize){
                gameBoardMatrix[i][j].revealed = true
            }
        }
    }

    fun setPlaceFlagMode() {
        placeFlagMode = true
    }

    fun getBoardSize() = boardSize

    fun setBoardSize(int: Int) {
        boardSize = int
    }

    fun getBoardContent(x: Int, y: Int) = gameBoardMatrix[x][y]

}