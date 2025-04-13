package com.example.chessclient.data

data class BoardState(val positions: Positions = Positions(), val whiteTurn: Boolean = true, val moveCount: Int = 0)