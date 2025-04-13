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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.example.chessclient.data.BoardState
import com.example.chessclient.ui.theme.ChessClientTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.min

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChessClientTheme {
                Board(BoardState())
            }
        }
    }
}

@Composable
fun Board(boardState: BoardState) {
    val context = LocalContext.current
    Canvas(modifier = Modifier.padding(top = 100.dp).fillMaxSize()) {
        val boardSize = min(size.width, size.height)
        var isLight = true
        for (i in 0..7) {
            for (j in 0..7) {
                drawRect(color = if (isLight) Color(222, 199, 169) else Color(133, 91, 54),
                        topLeft = Offset(x = j * (boardSize / 8), y = i * (boardSize / 8)),
                        size = Size(width = boardSize / 8, height = boardSize / 8)
                )
                val boardIndex = (j + (i * 8)).toULong()
                println(boardIndex)
                println(boardState.positions.blacks shl boardIndex.toInt())
                val image: Int? = if ((boardState.positions.blacks shr boardIndex.toInt()) and 1uL == 1uL) {
                    boardState.positions.pieceMap.entries
                                        .firstOrNull { pieceEntry -> (pieceEntry.value shr boardIndex.toInt()) and 1uL == 1uL }
                                        ?.key?.blackImageId
                } else if ((boardState.positions.whites shr boardIndex.toInt()) and 1uL == 1uL) {
                    boardState.positions.pieceMap.entries
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