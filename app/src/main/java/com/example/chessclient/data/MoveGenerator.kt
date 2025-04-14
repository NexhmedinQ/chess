package com.example.chessclient.data

private fun Pieces.generate(boardState: BoardState, pieceIndex: Int): List<Move> {
    return when(this) {
        Pieces.PAWN -> generatePawnMoves(boardState, pieceIndex)
        Pieces.QUEEN -> TODO()
        Pieces.KNIGHT -> TODO()
        Pieces.KING -> TODO()
        Pieces.BISHOP -> TODO()
        Pieces.ROOK -> TODO()
    }
}

fun generateMoves(boardState: BoardState, pieceIndex: Int): List<Move>? {
    val piece = boardState.positions.pieceMap.entries
        .find { (it.value shr pieceIndex) and 1uL != 0uL }
        ?.key
    return piece?.generate(boardState, pieceIndex)
}

private fun generatePawnMoves(boardState: BoardState, pieceIndex: Int): List<Move> {
    return emptyList()
}
