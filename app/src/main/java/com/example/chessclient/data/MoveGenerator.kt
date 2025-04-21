package com.example.chessclient.data

const val NORTH = -8
const val SOUTH = 8
const val EAST = 1
const val WEST = -1
const val RANK_1 = 0xFFuL
const val RANK_2 = 0xFF00uL
const val RANK_7 = 0x00FF000000000000uL
const val RANK_8 = 0xFF00000000000000uL
const val FILE_A = 0b1_0000_0001_0000_0001_0000_0001_0000_0001_0000_0001_0000_0001_0000_0001_0000_000uL
const val FILE_B = 0b0100_0000_0100_0000_0100_0000_0100_0000_0100_0000_0100_0000_0100_0000_0100_0000_uL
const val FILE_G = 0b0000_0010_0000_0010_0000_0010_0000_0010_0000_0010_0000_0010_0000_0010_0000_0010_uL
const val FILE_H = 0b0000_0001_0000_0001_0000_0001_0000_0001_0000_0001_0000_0001_0000_0001_0000_0001_uL

private fun Pieces.generate(boardState: BoardState, piece: ULong, player: Player): List<Move> {
    return when(this) {
        Pieces.PAWN -> generatePawnMoves(boardState, piece, player)
        Pieces.QUEEN -> generateQueenMove(boardState, piece, player)
        Pieces.KNIGHT -> generateKnightMoves(boardState, piece, player)
        Pieces.KING -> generateKingMoves(boardState, piece, player)
        Pieces.BISHOP -> generateBishopMoves(boardState, piece, player)
        Pieces.ROOK -> generateRookMoves(boardState, piece, player)
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
    val player = getPlayer(boardState, piece)
    return pieceType?.generate(boardState, piece, player)
}

private fun generateRookMoves(boardState: BoardState, piece: ULong, player: Player): List<Move> {
    // generate along rank and file in both directions (so need 4 calcs which will all be very similar)
    return emptyList()
}

private fun generateBishopMoves(boardState: BoardState, piece: ULong, player: Player): List<Move> {
    // generate along diag left to right and right to left
    return emptyList()
}

private fun generateQueenMove(boardState: BoardState, piece: ULong, player: Player): List<Move> {
    return generateBishopMoves(boardState, piece, player) + generateRookMoves(boardState, piece, player)
}


private fun generatePawnMoves(boardState: BoardState, piece: ULong, player: Player): List<Move> {
    return generatePawnCaptures(boardState, piece, player) + generateQuietPawnMoves(boardState, piece, player)
}

private fun generateKnightMoves(boardState: BoardState, piece: ULong, player: Player): List<Move> {
    var moveBoard = 0uL
    /*
    moves:
    - NNW -> rank2 and fileH
    - NWW -> rank1 and fileG
    - NNE -> rank2 and fileA
    - NEE -> rank1 and fileB
    - SSW -> rank7 and fileH
    - SWW -> rank8 and fileG
    - SSE -> rank7 and fileA
    - SEE -> rank8 and fileB
     */
    moveBoard = moveBoard xor shift(piece and (RANK_2 or RANK_1 or FILE_H).inv(), NORTH + NORTH + WEST)
    moveBoard = moveBoard xor shift(piece and (RANK_1 or FILE_G or FILE_H).inv(), NORTH + WEST + WEST)
    moveBoard = moveBoard xor shift(piece and (RANK_2 or RANK_1 or FILE_A).inv(), NORTH + NORTH + EAST)
    moveBoard = moveBoard xor shift(piece and (RANK_1 or FILE_B or FILE_A).inv(), NORTH + EAST + EAST)
    moveBoard = moveBoard xor shift(piece and (RANK_7 or RANK_8 or FILE_H).inv(), SOUTH + SOUTH + WEST)
    moveBoard = moveBoard xor shift(piece and (RANK_8 or FILE_G or FILE_H).inv(), SOUTH + WEST + WEST)
    moveBoard = moveBoard xor shift(piece and (RANK_7 or RANK_8 or FILE_A).inv(), SOUTH + SOUTH + EAST)
    moveBoard = moveBoard xor shift(piece and (RANK_8 or FILE_B or FILE_A).inv(), SOUTH + EAST + EAST)

    moveBoard = moveBoard and boardState.positions.colourMap.getValue(player).inv()
    return extractMoves(boardState, moveBoard, player, piece)
}

private fun generateKingMoves(boardState: BoardState, piece: ULong, player: Player): List<Move> {
    var moveBoard = 0uL
    moveBoard = moveBoard xor shift((RANK_1.inv() and piece), NORTH)
    moveBoard = moveBoard xor shift((RANK_8.inv() and piece), SOUTH)
    moveBoard = moveBoard xor shift((FILE_H.inv() and piece), WEST)
    moveBoard = moveBoard xor shift((FILE_A.inv() and piece), EAST)

    moveBoard = moveBoard xor shift((piece and (RANK_1 or FILE_H).inv()), NORTH + WEST)
    moveBoard = moveBoard xor shift((piece and (RANK_1 or FILE_A).inv()), NORTH + EAST)
    moveBoard = moveBoard xor shift((piece and (RANK_8 or FILE_H).inv()), SOUTH + WEST)
    moveBoard = moveBoard xor shift((piece and (RANK_8 or FILE_A).inv()), SOUTH + EAST)

    moveBoard = moveBoard and boardState.positions.colourMap.getValue(player).inv()
    return extractMoves(boardState, moveBoard, player, piece)
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

private fun shiftPawnPiece(moves: MutableList<Move>, player: Player, dir: Int, piece: ULong, colourMap: Map<Player, ULong>, relativeRankTwo: Int) {
    // single row move
    if (shift(piece, dir) and colourMap.getValue(!player) == 0uL
        && shift(piece, dir) and colourMap.getValue(player) == 0uL) {
        moves.add(Move(piece.countTrailingZeroBits(), shift(piece, dir).countTrailingZeroBits(), MoveType.QUIET))
    }
    // double shift when pawn is at starting pos
    if (piece.countTrailingZeroBits() / 8 == relativeRankTwo
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

private fun extractMoves(boardState: BoardState, bitboard: ULong, player: Player, piece: ULong): List<Move> {
    var board = bitboard
    var leadingCount = 0
    val moves = mutableListOf<Move>()
    while (board != 0uL) {
        val trailingZeros = board.countTrailingZeroBits()
        leadingCount += trailingZeros + 1
        moves.add(
            Move(from = piece.countTrailingZeroBits(),
            to = leadingCount - 1,
            moveType = if (boardState.positions.colourMap.getValue(!player) and (1uL shl (leadingCount - 1)) != 0uL) MoveType.CAPTURE else MoveType.QUIET
            )
        )
        board = board shr (trailingZeros + 1)
    }
    return moves
}

private fun getPlayer(boardState: BoardState, piece: ULong): Player {
    return if ((boardState.positions.colourMap.getValue(Player.BLACK) and piece) != 0uL) {
        Player.BLACK
    } else {
        Player.WHITE
    }
}
