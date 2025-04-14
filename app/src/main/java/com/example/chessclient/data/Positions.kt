package com.example.chessclient.data

data class Positions(val colourMap: Map<Player, ULong> = colourPositions(),
                     val pieceMap: Map<Pieces, ULong> = piecePositions()) {
    companion object {
        fun piecePositions(): Map<Pieces, ULong> {
            return mapOf(
                Pair(Pieces.PAWN, 0x00FF00000000FF00uL),
                Pair(Pieces.KNIGHT, 0x4200000000000042uL),
                Pair(Pieces.ROOK, 0x8100000000000081uL),
                Pair(Pieces.BISHOP, 0x2400000000000024uL),
                Pair(Pieces.QUEEN, 0x0800000000000008uL),
                Pair(Pieces.KING, 0x1000000000000010uL)
            )
        }

        fun colourPositions(): Map<Player, ULong> {
            return mapOf(
                Pair(Player.WHITE, 0xFFFF000000000000uL),
                Pair(Player.BLACK, 0x000000000000FFFFuL)
            )
        }
    }
}
