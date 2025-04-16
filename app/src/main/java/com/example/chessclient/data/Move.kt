package com.example.chessclient.data

enum class MoveType {
    QUIET,
    CAPTURE
}

data class Move(val from: Int, val to: Int, val moveType: MoveType)
