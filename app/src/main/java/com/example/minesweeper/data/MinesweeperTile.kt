package com.example.minesweeper.data

data class Tile(var type: Int, var minesAround: Int = 0,
                var isFlagged: Boolean = false, var wasClicked: Boolean = false,
                var revealed: Boolean = false)