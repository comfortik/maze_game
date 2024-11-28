package com.example.maze_game_app.maze

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.maze_game_app.maze.model.MazeActions
import com.example.maze_game_app.maze.model.MazeIntent
import com.example.maze_game_app.maze.model.MazePlayer
import org.koin.androidx.compose.koinViewModel

@Composable
fun MazeScreen(
    viewModel: MazeViewModel = koinViewModel()
) {

    val state = viewModel.state.collectAsState()

    LaunchedEffect(key1 = viewModel) {
        viewModel.actions.collect{ action->
            when(action){
                is MazeActions.ShowWinAlert->{
                    viewModel.handleIntent(MazeIntent.StartGame)
                }
            }
        }

    }

    MazeGame(rows = state.value.level.rows,
        cols = state.value.level.cols,
        radius = state.value.level.radius,
        maze = state.value.maze,
        exitCoordinates = state.value.exitCoordinates,
        player = state.value.player,
        onButtonClicked = viewModel::handleIntent
    )
}
@Composable
fun MazeGame(rows: Int, cols: Int, radius: Int, maze: Array<Array<Int>>, exitCoordinates: Pair<Int, Int>, player: MazePlayer, onButtonClicked:(MazeIntent)->Unit) {

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(12.dp)) {
        Spacer(modifier = Modifier.height(32.dp))
        MazeView(maze, exitCoordinates, player, rows, cols, radius)

        Spacer(modifier = Modifier.height(16.dp))
        MoveButtons (
            onButtonClicked
        )
    }
}


@Composable
private fun MoveButtons(
    onButtonClicked: (MazeIntent) -> Unit
) {
    val iconSize = 24.dp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {

        Button(
            onClick = { onButtonClicked(MazeIntent.MoveTop)},
            modifier = Modifier
                .size(64.dp)
                .border(2.dp, Color.Black, shape = RoundedCornerShape(topEnd = 2.dp, topStart = 8.dp, bottomEnd = 8.dp, bottomStart = 2.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
        ) {
            Icon(
                imageVector = Icons.Default.ThumbUp,
                contentDescription = "Up",
                modifier = Modifier.size(iconSize)
            )
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { onButtonClicked(MazeIntent.MoveLeft) },
                modifier = Modifier
                    .size(64.dp)
                    .border(4.dp, Color.Black, shape = RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Left",
                    modifier = Modifier.size(iconSize)
                )
            }
            Spacer(modifier = Modifier.width(64.dp))
            Button(
                onClick = { onButtonClicked(MazeIntent.MoveRight) },
                modifier = Modifier
                    .size(64.dp)
                    .border(4.dp, Color.Black, shape = RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Right",
                    modifier = Modifier.size(iconSize)
                )
            }
        }

        Button(
            onClick = {onButtonClicked(MazeIntent.MoveDown) },
            modifier = Modifier
                .size(64.dp)
                .border(4.dp, Color.Black, shape = RoundedCornerShape(12.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Down",
                modifier = Modifier.size(iconSize)
            )
        }
    }
}




@Composable
fun MazeView(
    maze: Array<Array<Int>>,
    exitCoordinates: Pair<Int, Int>,
    player: MazePlayer,
    rows: Int,
    cols: Int,
    radius: Int
) {
    val (exitX, exitY) = exitCoordinates

    Column {
        for (rowIndex in player.x - radius..player.x + radius) {
            Row {
                for (colIndex in player.y - radius..player.y + radius) {
                    when {
                        rowIndex == exitX && colIndex == exitY -> {

                            Box(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .weight(1f)
                            ) {
                                WallCanvas(
                                    rowIndex = rowIndex,
                                    colIndex = colIndex,
                                    maze = maze,
                                    background = Color.Green,
                                    rows = rows,
                                    cols = cols
                                )
                            }
                        }

                        rowIndex in -1..rows && colIndex in -1..cols &&
                                (rowIndex == -1 || colIndex == -1 || rowIndex == rows || colIndex == cols) -> {

                            Box(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .weight(1f)
                            ) {
                                WallCanvas(
                                    rowIndex = rowIndex,
                                    colIndex = colIndex,
                                    maze = maze,
                                    background = Color.Black,
                                    rows = rows,
                                    cols = cols
                                )
                            }
                        }

//

                        player.x == rowIndex && player.y == colIndex -> {
                            Box(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .weight(1f)
                                    .padding(5.dp)
                                    .background(Color.Blue)
                            )
                        }

                        maze.getOrNull(rowIndex)?.getOrNull(colIndex) == 1 -> {

                            Box(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .weight(1f)
                            ) {
                                WallCanvas(
                                    rowIndex = rowIndex,
                                    colIndex = colIndex,
                                    maze = maze,
                                    background = Color.Black,
                                    rows = rows,
                                    cols = cols
                                )
                            }
                        }

                        rowIndex in 0 until rows && colIndex in 0 until cols -> {
                            Box(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .weight(1f)
                                    .background(Color.White)
                            )
                        }

                        else -> {
                            // Серый цвет по умолчанию
                            Box(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .weight(1f)
                                    .background(Color.White)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WallCanvas(
    rowIndex: Int,
    colIndex: Int,
    maze: Array<Array<Int>>,
    rows: Int,
    cols: Int,
    background: Color
) {
    val strokeWidth = with(LocalDensity.current) { 4.dp.toPx() }

    androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {

        if (rowIndex == -1 && colIndex in 0 until cols) {
            drawLine(
                color = background,
                start = Offset(0f, size.height),
                end = Offset(size.width, size.height),
                strokeWidth = strokeWidth
            )
        } else if (rowIndex == rows && colIndex in 0 until cols) {

            drawLine(
                color = background,
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                strokeWidth = strokeWidth
            )
        } else if (colIndex == -1 && rowIndex in 0 until rows) {

            drawLine(
                color = background,
                start = Offset(size.width, 0f),
                end = Offset(size.width, size.height),
                strokeWidth = strokeWidth
            )
        } else if (colIndex == cols && rowIndex in 0 until rows) {
            drawLine(
                color = background,
                start = Offset(0f, 0f),
                end = Offset(0f, size.height),
                strokeWidth = strokeWidth
            )
        } else if (rowIndex in 0 until rows && colIndex in 0 until cols) {
            val top = maze.getOrNull(rowIndex - 1)?.getOrNull(colIndex) != 1
            val bottom = maze.getOrNull(rowIndex + 1)?.getOrNull(colIndex) != 1
            val left = maze.getOrNull(rowIndex)?.getOrNull(colIndex - 1) != 1
            val right = maze.getOrNull(rowIndex)?.getOrNull(colIndex + 1) != 1

            if (top) {
                drawLine(
                    color = background,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = strokeWidth
                )
            }

            if (bottom) {
                drawLine(
                    color = background,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = strokeWidth
                )
            }

            if (left) {
                drawLine(
                    color = background,
                    start = Offset(0f, 0f),
                    end = Offset(0f, size.height),
                    strokeWidth = strokeWidth
                )
            }

            if (right) {
                drawLine(
                    color = background,
                    start = Offset(size.width, 0f),
                    end = Offset(size.width, size.height),
                    strokeWidth = strokeWidth
                )
            }
        }
    }
}





fun isValidCell(x: Int, y: Int, rows: Int, cols: Int, maze: Array<Array<Int>>): Boolean {
    return x in 0 until rows && y in 0 until cols && maze[x][y] == 1
}

fun removeWallBetween(x1: Int, y1: Int, x2: Int, y2: Int, maze: Array<Array<Int>>) {
    maze[(x1 + x2) / 2][(y1 + y2) / 2] = 0
}