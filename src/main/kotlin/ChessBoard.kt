package org.example

class ChessBoard {
    private val bitBoard: MutableList<ULong> = mutableListOf(
        // White pieces
        0x00FF000000000000uL, // White pawns
        0x4200000000000000uL, // White knights
        0x2400000000000000uL, // White bishops
        0x8100000000000000uL, // White rooks
        0x0800000000000000uL, // White queen
        0x1000000000000000uL, // White king
        // Black pieces
        0x000000000000FF00uL, // Black pawns
        0x0000000000000042uL, // Black knights
        0x0000000000000024uL, // Black bishops
        0x0000000000000081uL, // Black rooks
        0x0000000000000008uL, // Black queen
        0x0000000000000010uL  // Black king
    )

    private var whiteToMove: Boolean = true
    private var moveCount: Int = 0
    private var canEnPassant: Boolean = false
}