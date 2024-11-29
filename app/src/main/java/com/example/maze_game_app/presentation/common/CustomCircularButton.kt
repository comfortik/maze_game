package com.example.maze_game_app.presentation.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Preview
@Composable
fun CustomCircularButton(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Black,
    centerSize: Float = 16f,
    onClick: ()->Unit = {}
) {
    val centerColor = lightenColor(backgroundColor, 0.1f)
    val bottomColor = lightenColor(backgroundColor, -0.4f)
    val topColor = lightenColor(backgroundColor, 0.4f)
    val startColor = lightenColor(backgroundColor, 0.2f)
    val endColor = lightenColor(backgroundColor, -0.1f)

    var padding by remember { mutableStateOf(12.dp) }

    Box(
        modifier = modifier
            .clickable { onClick() }
            .background(Color.Transparent, CircleShape)
            .onGloballyPositioned { coordinates ->
                val diameter = coordinates.size.width.dp
                padding = diameter / centerSize
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            val radiusOuter = size.width / 2
            val radiusInner = radiusOuter - padding.toPx()

            fun drawSector(
                startAngle: Float,
                sweepAngle: Float,
                color: Color
            ) {
                drawArc(
                    color = color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(center.x - radiusOuter, center.y - radiusOuter),
                    size = size.copy(width = radiusOuter * 2, height = radiusOuter * 2),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = radiusOuter - radiusInner)
                )
            }

            drawSector(
                startAngle = -45f,
                sweepAngle = 90f,
                color = topColor
            )

            drawSector(
                startAngle = 45f,
                sweepAngle = 90f,
                color = endColor
            )

            drawSector(
                startAngle = 135f,
                sweepAngle = 90f,
                color = bottomColor
            )

            drawSector(
                startAngle = 225f,
                sweepAngle = 90f,
                color = startColor
            )
        }
    }
}

