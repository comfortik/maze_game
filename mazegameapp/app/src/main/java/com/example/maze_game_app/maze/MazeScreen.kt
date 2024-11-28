package com.example.maze_game_app.maze

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.maze_game_app.common.CustomBoxTetris
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

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        MazeView(maze, exitCoordinates, player, rows, cols, radius)

        Spacer(modifier = Modifier.height(16.dp))


        Row {
            Button(onClick = { onButtonClicked(MazeIntent.MoveTop)}) {
                Text("Up")
            }
        }
        Row {
            Button(onClick = { onButtonClicked(MazeIntent.MoveLeft) }) {
                Text("Left")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { onButtonClicked(MazeIntent.MoveDown)}) {
                Text("Down")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { onButtonClicked(MazeIntent.MoveRight)}) {
                Text("Right")
            }
        }
    }
}




@Composable
fun MazeView(maze: Array<Array<Int>>, exitCoordinates: Pair<Int, Int>, player: MazePlayer, rows: Int, cols: Int, radius: Int,) {

    var (exitX, exitY) = exitCoordinates


    Column {
        for (rowIndex in player.x - radius..player.x + radius) {
            Row {
                for (colIndex in player.y - radius..player.y + radius) {
                    val (color, centerSize )= when {
                        rowIndex == exitX && colIndex == exitY -> Pair(Color.Green, 12f)
                        rowIndex in -1..rows && colIndex in -1..cols &&
                                (rowIndex == -1 || colIndex == -1 || rowIndex == rows || colIndex == cols) -> Pair(Color.Black, 12f)
                        rowIndex < 0 || colIndex < 0 || rowIndex >= rows || colIndex >= cols -> Pair(Color.Gray, 22f)
                        player.x == rowIndex && player.y == colIndex -> Pair(Color.Blue, 10f)
                        maze[rowIndex][colIndex] == 1 -> Pair(Color.Black, 12f)
                        else -> Pair(Color.LightGray, 20f)
                    }
                        Box(modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)){
                            CustomBoxTetris(backgroundColor = color, centerSize = centerSize)
                        }
                }
            }
        }
    }
}



@Preview
@Composable
fun PreviewCustomBox(){
    Row {


        Box(modifier = Modifier
            .weight(1f)
            .aspectRatio(1f)){
            CustomBoxTetris()

        }
        Box(modifier = Modifier
            .weight(1f)
            .aspectRatio(1f)){
            CustomBoxTetris()
        }

    }

}

fun isValidCell(x: Int, y: Int, rows: Int, cols: Int, maze: Array<Array<Int>>): Boolean {
    return x in 0 until rows && y in 0 until cols && maze[x][y] == 1
}

fun removeWallBetween(x1: Int, y1: Int, x2: Int, y2: Int, maze: Array<Array<Int>>) {
    maze[(x1 + x2) / 2][(y1 + y2) / 2] = 0
}