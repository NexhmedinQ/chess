package org.example

enum class Pieces {
    PAWN {
        override fun validMove(board: ChessBoard, move: Pair<Coordinate, Coordinate>): Boolean {
            return true
        }
    },
    QUEEN {
        override fun validMove(board: ChessBoard, move: Pair<Coordinate, Coordinate>): Boolean {
            return true
        }
    },
    KING {
        override fun validMove(board: ChessBoard, move: Pair<Coordinate, Coordinate>): Boolean {
            return true
        }
    },
    BISHOP {
        override fun validMove(board: ChessBoard, move: Pair<Coordinate, Coordinate>): Boolean {
            return true
        }
    },
    KNIGHT {
        override fun validMove(board: ChessBoard, move: Pair<Coordinate, Coordinate>): Boolean {
            return true
        }
    },
    ROOK {
        override fun validMove(board: ChessBoard, move: Pair<Coordinate, Coordinate>): Boolean {
            return true
        }
    };
    abstract fun validMove(board: ChessBoard, move: Pair<Coordinate, Coordinate>) : Boolean

    companion object {
        fun pieceType(board: ChessBoard, coordinate: Coordinate): Pieces? {
            return PAWN
        }
    }
}