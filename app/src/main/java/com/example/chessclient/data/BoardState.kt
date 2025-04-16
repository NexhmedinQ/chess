package com.example.chessclient.data

data class BoardState(val positions: Positions = Positions(),
                      val playerTurn: Player = Player.WHITE, val moveCount: Int = 0)

enum class Player {
    WHITE,
    BLACK
    ;
    operator fun not(): Player {
        return when(this) {
            WHITE -> BLACK
            BLACK -> WHITE
        }
    }
}