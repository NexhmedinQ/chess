package com.example.chessclient.presentation

import androidx.lifecycle.ViewModel
import com.example.chessclient.data.Player
import com.example.chessclient.data.UIBoardState
import com.example.chessclient.data.UIState
import com.example.chessclient.data.generateMoves
import com.example.chessclient.data.makeMove
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ChessClientViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(UIBoardState())
    val uiState: StateFlow<UIBoardState> = _uiState.asStateFlow()

    fun onTap(tappedField: ULong, player: Player) {
        when (_uiState.value.uiState) {
            UIState.WAITING -> {}
            UIState.TURN -> {
                if (player == _uiState.value.boardState.playerTurn) {
                    updateMoveGen(tappedField)
                }
            }
            UIState.CLICKED -> {
                // if clicking opponent piece / square you are able to move to, move to the square and make backend call
                val move = uiState.value.possibleMoves
                    ?.find { it.to == tappedField.countTrailingZeroBits() }
                if (move != null) {
                    val newPositions = makeMove(move, uiState.value.boardState.positions, player)
                    _uiState.update {
                        // NEED TO CHANGE STATE TO WAITING IN FUTURE
                        it.copy(boardState = it.boardState.copy(positions = newPositions), possibleMoves = null, uiState = UIState.TURN)
                    }
                }
                // if clicking own piece
                else if (tappedField and uiState.value.boardState.positions.colourMap.getValue(player) != 0uL) {
                    if (tappedField.countTrailingZeroBits() == uiState.value.clicked) {
                        _uiState.update {
                            it.copy(possibleMoves = null, clicked = null, uiState = UIState.TURN)
                        }
                    } else {
                        updateMoveGen(tappedField)
                    }
                }

            }
        }
    }

    private fun updateMoveGen(tappedField: ULong) {
        val moves = generateMoves(uiState.value.boardState, tappedField)
        _uiState.update {
            it.copy(possibleMoves = moves, uiState = UIState.CLICKED, clicked = tappedField.countTrailingZeroBits())
        }
    }
}