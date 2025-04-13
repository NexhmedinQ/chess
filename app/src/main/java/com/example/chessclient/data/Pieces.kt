package com.example.chessclient.data

import com.example.chessclient.R

enum class Pieces(val whiteImageId: Int, val blackImageId: Int) {
    PAWN(R.drawable.pawn_white, R.drawable.pawn_black),
    QUEEN(R.drawable.queen_white, R.drawable.queen_black),
    KNIGHT(R.drawable.knight_white, R.drawable.knight_black),
    KING(R.drawable.king_white, R.drawable.king_black),
    BISHOP(R.drawable.bishop_white, R.drawable.bishop_black),
    ROOK(R.drawable.rook_white, R.drawable.rook_black)
}