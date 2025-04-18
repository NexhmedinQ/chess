package com.example.chessclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.example.chessclient.data.Player
import com.example.chessclient.presentation.ChessClientViewModel
import com.example.chessclient.ui.theme.ChessClientTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chessclient.data.UIBoardState
import kotlin.math.min

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel = hiltViewModel<ChessClientViewModel>()
            val state by viewModel.uiState.collectAsState()
            ChessClientTheme {
                Board(state, Player.WHITE, viewModel)
            }
        }
    }
}

@Composable
fun Board(state: UIBoardState, player: Player, viewModel: ChessClientViewModel) {
    val context = LocalContext.current
    Canvas(modifier = Modifier
        .padding(top = 100.dp)
        .fillMaxSize()
        .pointerInput(true) {
            detectTapGestures {
                val boardSize = min(size.width, size.height)
                val x = (8 * it.x / boardSize).toInt()
                val y = (8 * it.y / boardSize).toInt()
                val tappedField = 8 * y + x
                viewModel.onTap(1uL shl tappedField, player)
            }
        }
    ) {
        val boardSize = min(size.width, size.height)
        var isLight = true
        for (i in 0..7) {
            for (j in 0..7) {
                state.possibleMoves
                    ?.firstOrNull { it.to == (i * 8) + j }
                    ?.let {
                    drawRect(color = Color(0, 199, 0),
                    topLeft = Offset(x = j * (boardSize / 8), y = i * (boardSize / 8)),
                    size = Size(width = boardSize / 8, height = boardSize / 8))
                } ?:
                drawRect(color = if (isLight) Color(222, 199, 169) else Color(133, 91, 54),
                        topLeft = Offset(x = j * (boardSize / 8), y = i * (boardSize / 8)),
                        size = Size(width = boardSize / 8, height = boardSize / 8)
                )

                val boardIndex = (j + (i * 8)).toULong()
                val image: Int? =
                    if ((state.boardState.positions.colourMap.getValue(Player.BLACK) shr boardIndex.toInt()) and 1uL == 1uL) {
                        state.boardState.positions.pieceMap.entries
                        .firstOrNull { pieceEntry -> (pieceEntry.value shr boardIndex.toInt()) and 1uL == 1uL }
                        ?.key?.blackImageId
                } else if ((state.boardState.positions.colourMap.getValue(Player.WHITE) shr boardIndex.toInt()) and 1uL == 1uL) {
                        state.boardState.positions.pieceMap.entries
                        .firstOrNull { pieceEntry -> (pieceEntry.value shr boardIndex.toInt()) and 1uL == 1uL }
                        ?.key?.whiteImageId
                } else {
                    null
                }

                image
                    ?.let { drawImage(
                        image = ImageBitmap.imageResource(id = image, res = context.resources),
                        topLeft = Offset(x = j * (boardSize / 8), y = i * (boardSize / 8))) }
                isLight = !isLight
            }
            isLight = !isLight
        }

    }
}