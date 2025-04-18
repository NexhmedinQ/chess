package com.example.chessclient.data

const val NORTH = -8
const val SOUTH = 8
const val EAST = 1
const val WEST = -1

private fun Pieces.generate(boardState: BoardState, piece: ULong): List<Move> {
    return when(this) {
        Pieces.PAWN -> generatePawnMoves(boardState, piece)
        Pieces.QUEEN -> TODO()
        Pieces.KNIGHT -> TODO()
        Pieces.KING -> TODO()
        Pieces.BISHOP -> TODO()
        Pieces.ROOK -> TODO()
    }
}

fun makeMove(move: Move, positions: Positions, player: Player): Positions {
    val piece = 1uL shl move.from
    val pieceType = positions.pieceMap.entries
        .find { (it.value and piece) != 0uL }
        ?.key!!
    val newPieceMap = positions.copy().pieceMap.toMutableMap()
    val newColourMap = positions.copy().colourMap.toMutableMap()
    // remove old position
    newPieceMap[pieceType] = newPieceMap[pieceType]!! xor piece
    newColourMap[player] = newColourMap[player]!! xor piece
    // add new position
    val newPiece = 1uL shl move.to
    newPieceMap[pieceType] = newPieceMap[pieceType]!! or newPiece
    newColourMap[player] = newColourMap[player]!! or newPiece

    // remove captured piece
    if (move.moveType == MoveType.CAPTURE) {
        newColourMap[!player] = newColourMap[!player]!! xor newPiece
        val capturedPieceType = positions.pieceMap.entries
            .find { (it.value and newPiece) != 0uL }
            ?.key!!
        if (capturedPieceType != pieceType) {
            newPieceMap[capturedPieceType] = newPieceMap[capturedPieceType]!! xor newPiece
        }
    }
    return Positions(newColourMap, newPieceMap)
}

fun generateMoves(boardState: BoardState, piece: ULong): List<Move>? {
    val pieceType = boardState.positions.pieceMap.entries
        .find { (it.value and piece) != 0uL }
        ?.key
    return pieceType?.generate(boardState, piece)
}

private fun generatePawnMoves(boardState: BoardState, piece: ULong): List<Move> {
    val player = if ((boardState.positions.colourMap.getValue(Player.BLACK) and piece) != 0uL) {
        Player.BLACK
    } else {
        Player.WHITE
    }
    return generatePawnCaptures(boardState, piece, player) + generateQuietPawnMoves(boardState, piece, player)
}

private fun generatePawnCaptures(boardState: BoardState, piece: ULong, player: Player): List<Move> {
    return mutableListOf<Move>().apply {
        when(player) {
            Player.WHITE -> {
                generatePawnCaptureHelper(NORTH, this, boardState, piece, player)
            }
            Player.BLACK -> {
                generatePawnCaptureHelper(SOUTH, this, boardState, piece, player)
            }
        }
    }
}

private fun generatePawnCaptureHelper(dir: Int, moves: MutableList<Move>, boardState: BoardState, piece: ULong, player: Player) {
    if (piece.countTrailingZeroBits() % 8 != 0 && (shift(piece, dir + WEST) and boardState.positions.colourMap.getValue(!player) != 0uL)) {
        moves.add(Move(piece.countTrailingZeroBits(), shift(piece, dir + WEST).countTrailingZeroBits(), MoveType.CAPTURE))
    }
    if (shift(piece, dir + EAST) and boardState.positions.colourMap.getValue(!player) != 0uL) {
        moves.add(Move(piece.countTrailingZeroBits(), shift(piece, dir + EAST).countTrailingZeroBits(), MoveType.CAPTURE))
    }
}

private fun generateQuietPawnMoves(boardState: BoardState, piece: ULong, player: Player): List<Move> {
    return mutableListOf<Move>().apply {
        when(player) {
            Player.WHITE -> {
                shiftPawnPiece(this, player, NORTH, piece, boardState.positions.colourMap, 6)
            }
            Player.BLACK -> {
                shiftPawnPiece(this, player, SOUTH, piece, boardState.positions.colourMap, 1)
            }
        }
    }
}

private fun shiftPawnPiece(moves: MutableList<Move>, player: Player, dir: Int, piece: ULong, colourMap: Map<Player, ULong>, rowTwo: Int) {
    // single row move
    if (shift(piece, dir) and colourMap.getValue(!player) == 0uL
        && shift(piece, dir) and colourMap.getValue(player) == 0uL) {
        moves.add(Move(piece.countTrailingZeroBits(), shift(piece, dir).countTrailingZeroBits(), MoveType.QUIET))
    }
    // double shift when pawn is at starting pos
    if (piece.countTrailingZeroBits() / 8 == rowTwo
        && (shift(piece, dir + dir) and colourMap.getValue(!player) == 0uL)
        && (shift(piece, dir + dir) and colourMap.getValue(player) == 0uL)) {
        moves.add(Move(piece.countTrailingZeroBits(), shift(piece, dir + dir).countTrailingZeroBits(), MoveType.QUIET))
    }
}

private fun shift(piece: ULong, n: Int): ULong {
    return if (n >= 0) {
        piece shl(n)
    } else {
        piece shr(-n)
    }

}
