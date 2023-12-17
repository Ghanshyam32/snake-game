package com.ghanshyam.tailtrail

import android.icu.text.ListFormatter.Width
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.ghanshyam.tailtrail.ui.theme.Custard
import com.ghanshyam.tailtrail.ui.theme.RoyalBlue

@Composable
fun SnakeGameScreen(
    state: SnakeGameState
) {
    val foodImage = ImageBitmap.imageResource(id = R.drawable.food)
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Score: ${state.snake.size - 1}",
                style = MaterialTheme.typography.headlineMedium
            )
        }
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ratio = 2 / 3f)
        ) {
            val cellSize = size.width / 20
            drawGameBoard(
                cellSize = cellSize,
                cellColor = Custard,
                borderCellColor = RoyalBlue,
                gridWidth = state.xAxisGridSize,
                gridHeight = state.yAxisGridSize
            )
            drawFood(
                foodImage = foodImage,
                cellSize = cellSize.toInt(),
                coordinate = state.food
            )
        }
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = { /*TODO*/ }) {
                Text(text = "Replay")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                modifier = Modifier.weight(1f),
                onClick = { /*TODO*/ }) {
                Text(text = "Start")
            }
        }
    }
}

private fun DrawScope.drawGameBoard(
    cellSize: Float,
    cellColor: Color,
    borderCellColor: Color,
    gridWidth: Int,
    gridHeight: Int
) {
    for (i in 0 until gridWidth) {
        for (j in 0 until gridHeight) {
            val isBorderCell = i == 0 || j == 0 || i == gridWidth - 1 || j == gridHeight - 1
            drawRect(
                color = if (isBorderCell) borderCellColor
                else if ((i + j) % 2 == 0) cellColor
                else cellColor.copy(alpha = 0.5f),
                topLeft = Offset(x = i * cellSize, y = j * cellSize),
                size = Size(cellSize, cellSize)
            )
        }
    }
}

private fun DrawScope.drawFood(
    foodImage: ImageBitmap,
    cellSize: Int,
    coordinate: coordinate
) {
    drawImage(
        image = foodImage,
        dstOffset = IntOffset(
            x = (coordinate.x * cellSize),
            y = (coordinate.y * cellSize)
        ),
        dstSize = IntSize(cellSize, cellSize)
    )
}