package com.example.chessclient.data

const val NORTH = -8
const val SOUTH = 8
const val EAST = 1
const val WEST = -1

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
    val player = if ((boardState.positions.colourMap.getValue(Player.BLACK) shr pieceIndex) and 1uL == 1uL) {
        Player.BLACK
    } else {
        Player.WHITE
    }
    return generatePawnCaptures(boardState, pieceIndex, player) + generateQuietPawnMoves(boardState, pieceIndex, player)
}

private fun generatePawnCaptures(boardState: BoardState, pieceIndex: Int, player: Player): List<Move> {
    return emptyList()
}

private fun generateQuietPawnMoves(boardState: BoardState, pieceIndex: Int, player: Player): List<Move> {
    mutableListOf<Move>().apply {
        when(player) {
            Player.WHITE -> {
                shiftPawnPiece(this, player, NORTH, pieceIndex, boardState.positions.colourMap)
            }
            Player.BLACK -> {
                shiftPawnPiece(this, player, SOUTH, pieceIndex, boardState.positions.colourMap)
            }
        }
    }
}

private fun shiftPawnPiece(moves: MutableList<Move>, player: Player, dir: Int, pieceIndex: Int, colourMap: Map<Player, ULong>) {
    // single row move
    if (shift(pieceIndex, dir) and colourMap.getValue(!player).toInt() != 0) {
        moves.add(Move(pieceIndex, shift(pieceIndex, dir), MoveType.QUIET))
    }
    // double shift when pawn is at starting pos
    if (pieceIndex / 8 == 6 && (shift(pieceIndex, dir + dir) and colourMap.getValue(!player).toInt() != 0)) {
        moves.add(Move(pieceIndex, shift(pieceIndex, dir + dir), MoveType.QUIET))
    }
}

private fun shift(board: Int, n: Int): Int {
    return if (n >= 0) {
        board shr(n)
    } else {
        board shl(-n)
    }

}
