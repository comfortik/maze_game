package com.example.maze_game_app.presentation.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.max

@Preview
@Composable
fun DiagonalDots(modifier: Modifier = Modifier.size(200.dp)) {
    Canvas(modifier = modifier) {
        val rows = 3
        val columns = 3

        val spacing = size.minDimension / (max(rows, columns).toFloat() + 1)
        val dotRadius = spacing / 6

        val offsetX = size.width / 2 - (columns - 1) * spacing / 2
        val offsetY = size.height / 2 - (rows - 1) * spacing / 2

        for (row in 0 until rows) {
            for (column in 0 until columns) {
                val isOddRow = row % 2 != 0
                val x = (column + if (isOddRow) 0.5f else 0f) * spacing
                val y = row * spacing


                val rotatedX = (x - offsetX) * 0.7071f - (y - offsetY) * 0.7071f + offsetX
                val rotatedY = (x - offsetX) * 0.7071f + (y - offsetY) * 0.7071f + offsetY

                drawCircle(
                    color = Color.Black,
                    radius = dotRadius,
                    center = Offset(rotatedX, rotatedY)
                )
            }
        }
    }
}

