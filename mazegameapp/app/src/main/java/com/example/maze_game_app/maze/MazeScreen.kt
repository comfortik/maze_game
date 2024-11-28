package com.example.maze_game_app.maze

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.maze_game_app.R
import com.example.maze_game_app.common.CustomBoxTetris
import com.example.maze_game_app.common.CustomCircularButton
import com.example.maze_game_app.common.DiagonalDots
import com.example.maze_game_app.maze.model.MazeActions
import com.example.maze_game_app.maze.model.MazeIntent
import com.example.maze_game_app.maze.model.MazePlayer
import org.koin.androidx.compose.koinViewModel

@Composable
fun MazeScreen(
    viewModel: MazeViewModel = koinViewModel(),
    innerPadding: PaddingValues= PaddingValues()
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
        onButtonClicked = viewModel::handleIntent,
        innerPadding= innerPadding,
        level = state.value.level.num
    )

}
@Composable
fun MazeGame(rows: Int, cols: Int, radius: Int, maze: Array<Array<Int>>, exitCoordinates: Pair<Int, Int>, player: MazePlayer, onButtonClicked:(MazeIntent)->Unit, innerPadding: PaddingValues, level: Int) {

    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF222222))
            .padding(innerPadding)) {
        Box(
            modifier = Modifier
                .aspectRatio(1f)

        ) {
            CustomBoxTetris(backgroundColor = Color.Black, centerSize = 20f)
            MazeView(maze, exitCoordinates, player, rows, cols, radius)
        }

        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "level: $level",
            fontFamily = FontFamily.Monospace,
            style = TextStyle(color = Color.White),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(12.dp)

        ) {
            CustomBoxTetris(backgroundColor = Color.Black, modifier = Modifier.fillMaxSize(), centerSize = 50f)

        }

        Spacer(modifier = Modifier.height(32.dp))



        Row(modifier = Modifier.fillMaxWidth().padding(end = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ArrowButtons(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .fillMaxSize(0.45f),
                onButtonClicked = onButtonClicked
            )
            Box(
                modifier = Modifier
                    .fillMaxSize(0.4f)
            ) {

                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val shadowColor = Color.Black.copy(alpha = 0.2f)

                    val rotationAngle = 25f

                    val rectWidth = size.width
                    val rectHeight = size.height
                    val cornerRadius = 100f

                    val centerX = size.width/1.9f
                    val centerY = size.height/0.95f


                    withTransform({
                        rotate(
                            degrees = rotationAngle,
                            pivot = Offset(centerX, centerY)
                        )
                    }) {

                        drawRoundRect(
                            color = shadowColor,
                            topLeft = Offset(
                                x = centerX - rectWidth / 1,
                                y = centerY - rectHeight / 1
                            ),
                            size = androidx.compose.ui.geometry.Size(rectWidth, rectHeight),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius, cornerRadius)
                        )
                    }
                }

                // Первая кнопка
                CustomCircularButton(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .fillMaxSize(0.55f)
                )

                // Вторая кнопка
                CustomCircularButton(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxSize(0.55f)
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        DiagonalDots(
            Modifier
                .size(130.dp)
                .align(Alignment.Start)
                .padding(top = 42.dp, start = 64.dp)
        )

    }
}




@Composable
fun MazeView(maze: Array<Array<Int>>, exitCoordinates: Pair<Int, Int>, player: MazePlayer, rows: Int, cols: Int, radius: Int,) {

    var (exitX, exitY) = exitCoordinates


    Column(modifier = Modifier.padding(16.dp)) {
        for (rowIndex in player.x - radius..player.x + radius) {
            Row {
                for (colIndex in player.y - radius..player.y + radius) {
                    val (color, centerSize )= when {
                        rowIndex == exitX && colIndex == exitY -> Pair(Color.Green, 12f)
                        rowIndex in 0..rows && colIndex in 0..cols &&
                                (rowIndex == 0 || colIndex == 0 || rowIndex == rows || colIndex == cols) -> Pair(Color.Black, 12f)
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



@Composable
fun ArrowButtons(
    modifier: Modifier,
    onButtonClicked: (MazeIntent) -> Unit
) {
    Column (
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center){
            TetrisBoxWithIcon(icon = painterResource(id = R.drawable.pixel_arrow),
                modifier = Modifier.fillMaxWidth(0.33f),
                boxColor = Color.Black,
                iconModifier = Modifier
                    .fillMaxSize(0.4f)
                    .graphicsLayer(rotationZ = -90f),
                iconColor = Color.Gray){
                onButtonClicked(MazeIntent.MoveTop)
            }
        }

        Row (modifier = Modifier.fillMaxWidth()){
            TetrisBoxWithIcon(icon = painterResource(id = R.drawable.pixel_arrow),
                modifier = Modifier.weight(1f),
                boxColor = Color.Black,
                iconModifier = Modifier
                    .fillMaxSize(0.4f)
                    .graphicsLayer(rotationZ = 180f),
                iconColor = Color.Gray){
                onButtonClicked(MazeIntent.MoveLeft)
            }

            Box(modifier = Modifier
                .weight(1f)
                .aspectRatio(1f)){
                CustomBoxTetris(backgroundColor = Color.Black,)
            }

            TetrisBoxWithIcon(icon = painterResource(id = R.drawable.pixel_arrow),
                modifier = Modifier.weight(1f),
                boxColor = Color.Black,
                iconModifier = Modifier
                    .fillMaxSize(0.4f),
                iconColor = Color.Gray){
                onButtonClicked(MazeIntent.MoveRight)
            }

        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
            TetrisBoxWithIcon(icon = painterResource(id = R.drawable.pixel_arrow),
                modifier = Modifier.fillMaxWidth(0.33f),
                boxColor = Color.Black,
                iconModifier = Modifier
                    .fillMaxSize(0.4f)
                    .graphicsLayer(rotationZ = 90f),
                iconColor = Color.Gray){
                onButtonClicked(MazeIntent.MoveDown)
            }
        }
    }
}

@Composable
fun TetrisBoxWithIcon(
    icon: Painter,
    modifier: Modifier,
    boxColor: Color,
    iconModifier: Modifier,
    iconColor: Color,
    onClick:()->Unit
                      ){
    Box(
        modifier = Modifier
            .clickable { onClick() }
            .then(modifier)
            .aspectRatio(1f)

    ) {
        CustomBoxTetris(backgroundColor = boxColor)
        Icon(
            painter = icon,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .then(iconModifier) ,
            tint = iconColor
        )
    }
}


fun isValidCell(x: Int, y: Int, rows: Int, cols: Int, maze: Array<Array<Int>>): Boolean {
    return x in 0 until rows && y in 0 until cols && maze[x][y] == 1
}

fun removeWallBetween(x1: Int, y1: Int, x2: Int, y2: Int, maze: Array<Array<Int>>) {
    maze[(x1 + x2) / 2][(y1 + y2) / 2] = 0
}