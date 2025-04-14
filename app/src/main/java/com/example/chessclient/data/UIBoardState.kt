package com.example.chessclient.data

data class UIBoardState(val boardState: BoardState = BoardState(),
                        val clicked: Int? = null,
                        val possibleMoves: List<Move>? = null,
                        val uiState: UIState = UIState.TURN)