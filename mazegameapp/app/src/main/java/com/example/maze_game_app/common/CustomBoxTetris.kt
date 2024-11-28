package com.example.maze_game_app.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp






@Preview
@Composable
fun CustomBoxTetris(
    modifier: Modifier = Modifier.aspectRatio(1f).fillMaxSize(),
    backgroundColor: Color = Color.Blue,
    centerSize: Float = 12f
) {

    val centerColor = lightenColor(backgroundColor, 0.1f)
    val bottomColor = lightenColor(backgroundColor, -0.4f)
    val topColor = lightenColor(backgroundColor, 0.4f )
    val startColor = lightenColor(backgroundColor, 0.2f)
    val endColor = lightenColor(backgroundColor, -0.1f)


    var padding by remember { mutableStateOf(48.dp) }

    Box(
        modifier = modifier
            .background(centerColor)
            .aspectRatio(1f)
            .onGloballyPositioned { coordinates ->

                val width = coordinates.size.width.dp
                padding = width / centerSize
            }
    )  {
        Canvas(modifier = Modifier.fillMaxSize()) {


            val innerRect = Rect(
                left = padding.toPx(),
                top = padding.toPx(),
                right = size.width - padding.toPx(),
                bottom = size.height - padding.toPx()
            )

            val outerRect = Rect(
                left = 0f,
                top = 0f,
                right = size.width,
                bottom = size.height
            )

            val topLeftOuter = outerRect.topLeft
            val topRightOuter = outerRect.topRight
            val bottomLeftOuter = outerRect.bottomLeft
            val bottomRightOuter = outerRect.bottomRight

            val topLeftInner = innerRect.topLeft
            val topRightInner = innerRect.topRight
            val bottomLeftInner = innerRect.bottomLeft
            val bottomRightInner = innerRect.bottomRight

            fun drawTrapezoid(
                outerCorner1: Offset,
                outerCorner2: Offset,
                innerCorner1: Offset,
                innerCorner2: Offset,
                color: Color
            ) {
                val path = Path().apply {
                    moveTo(outerCorner1.x, outerCorner1.y)
                    lineTo(outerCorner2.x, outerCorner2.y)
                    lineTo(innerCorner2.x, innerCorner2.y)
                    lineTo(innerCorner1.x, innerCorner1.y)
                    close()
                }
                drawPath(path, color)
            }


            drawTrapezoid(
                outerCorner1 = topLeftOuter,
                outerCorner2 = topRightOuter,
                innerCorner1 = topLeftInner,
                innerCorner2 = topRightInner,
                color = topColor
            )

            drawTrapezoid(
                outerCorner1 = topRightOuter,
                outerCorner2 = bottomRightOuter,
                innerCorner1 = topRightInner,
                innerCorner2 = bottomRightInner,
                color = endColor
            )

            drawTrapezoid(
                outerCorner1 = bottomRightOuter,
                outerCorner2 = bottomLeftOuter,
                innerCorner1 = bottomRightInner,
                innerCorner2 = bottomLeftInner,
                color = bottomColor
            )

            drawTrapezoid(
                outerCorner1 = bottomLeftOuter,
                outerCorner2 = topLeftOuter,
                innerCorner1 = bottomLeftInner,
                innerCorner2 = topLeftInner,
                color = startColor
            )
        }
    }
}


fun lightenColor(color: Color, factor: Float): Color {
    require(factor in -1f..1f) { "Factor must be between -1 and 1" }

    val adjustedFactor = if (factor > 0) factor else factor * -1

    val red = if (factor > 0) {
        (color.red + (1 - color.red) * adjustedFactor).coerceIn(0f, 1f)
    } else {
        (color.red * (1 - adjustedFactor)).coerceIn(0f, 1f)
    }

    val green = if (factor > 0) {
        (color.green + (1 - color.green) * adjustedFactor).coerceIn(0f, 1f)
    } else {
        (color.green * (1 - adjustedFactor)).coerceIn(0f, 1f)
    }

    val blue = if (factor > 0) {
        (color.blue + (1 - color.blue) * adjustedFactor).coerceIn(0f, 1f)
    } else {
        (color.blue * (1 - adjustedFactor)).coerceIn(0f, 1f)
    }

    return Color(red, green, blue, alpha = color.alpha)
}
