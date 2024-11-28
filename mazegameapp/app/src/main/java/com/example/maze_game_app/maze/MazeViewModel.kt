package com.example.maze_game_app.maze

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.maze_game_app.maze.model.MazeActions
import com.example.maze_game_app.maze.model.MazeIntent
import com.example.maze_game_app.maze.model.MazeLevel
import com.example.maze_game_app.maze.model.MazePlayer
import com.example.maze_game_app.maze.model.MazeUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class MazeViewModel: ViewModel() {


    private val _state = MutableStateFlow(
        MazeUiState(
            player = MazePlayer(21 / 2, 21 / 2),
            isWin = false,
            level = defaultLevel,
            exitCoordinates = Pair(0,0),
            maze = arrayOf()
        )
    )
    init {
        val (firstMaze, exits) = generateMaze(42, 42)
        _state.value = MazeUiState(
            player = MazePlayer(42 / 2, 42 / 2),
            isWin = false,
            level = levels[1]?: defaultLevel,
            exitCoordinates = exits,
            maze = firstMaze
        )
    }
    val state = _state.asStateFlow()

    private val _intent = MutableSharedFlow<MazeIntent>()
    val intent = _intent.asSharedFlow()

    private val _actions = MutableSharedFlow<MazeActions>(replay = 1)
    val actions = _actions.asSharedFlow()

    fun handleIntent(intent: MazeIntent){
        when(intent){
            is MazeIntent.MoveTop->{
                movePlayer(state.value.player, maze = state.value.maze, dx = -1, dy = 0, exitCoordinates = state.value.exitCoordinates)
            }
            is MazeIntent.MoveLeft -> {
                movePlayer(state.value.player, maze = state.value.maze, dx = 0, dy = -1, exitCoordinates = state.value.exitCoordinates)
            }
            is MazeIntent.MoveDown -> {
                movePlayer(state.value.player, maze = state.value.maze, dx = 1, dy = 0, exitCoordinates = state.value.exitCoordinates)
            }
            is MazeIntent.MoveRight -> {
               movePlayer(state.value.player, maze = state.value.maze, dx = 0, dy = 1, exitCoordinates = state.value.exitCoordinates)
            }
            is MazeIntent.StartGame -> {

                val newLevel = if(state.value.level.num< levels.size){
                    levels[state.value.level.num+1]
                }else{
                    levels[state.value.level.num]
                }
                val newRows = newLevel?.rows?:21
                val newCols = newLevel?.cols?:21

                val (maze, exitC) = generateMaze(newRows, newCols)
                _state.value = MazeUiState(
                    player = MazePlayer(newRows/2, newCols/2),
                    isWin = false,
                    level = newLevel?: defaultLevel,
                    exitCoordinates = exitC,
                    maze = maze
                )
            }
        }
    }
    fun movePlayer(player: MazePlayer, maze: Array<Array<Int>>, dx: Int, dy: Int, exitCoordinates: Pair<Int, Int>) {
        val newX = player.x + dx
        val newY = player.y + dy

        if (newX in maze.indices && newY in maze[0].indices && maze[newX][newY] == 0) {

            _state.value = state.value.copy(
                player = MazePlayer(newX, newY)
            )
            Log.d("Player moved", "New position: $newX, $newY")
        }
        else if(exitCoordinates.first==newX && exitCoordinates.second==newY){
            _state.value = state.value.copy(
                player = MazePlayer(newX, newY)
            )
            val emitted = _actions.tryEmit(MazeActions.ShowWinAlert("You win in ${state.value.level}"))
            Log.d("Action emitted", "Success: $emitted")

        }
        else {
            Log.d("Player move blocked", "Blocked position: $newX, $newY")
        }
    }
    fun generateMaze(rows: Int, cols: Int): Pair<Array<Array<Int>>, Pair<Int, Int> >{
        val maze = Array(rows) { Array(cols) { 1 } }
        val stack = mutableListOf<Pair<Int, Int>>()
        val directions = listOf(Pair(0, -2), Pair(0, 2), Pair(-2, 0), Pair(2, 0))


        for (i in 0 until rows) {
            maze[i][0] = 1
            maze[i][cols - 1] = 1
        }
        for (j in 0 until cols) {
            maze[0][j] = 1
            maze[rows - 1][j] = 1
        }


        var current = Pair(rows / 2, cols / 2)
        stack.add(current)
        maze[current.first][current.second] = 0


        while (stack.isNotEmpty()) {
            val (x, y) = current
            val neighbors = directions.shuffled().filter {
                isValidCell(x + it.first, y + it.second, rows, cols, maze)
            }

            if (neighbors.isNotEmpty()) {
                val next = neighbors.random()
                val nx = x + next.first
                val ny = y + next.second

                removeWallBetween(x, y, nx, ny, maze)
                maze[nx][ny] = 0
                stack.add(Pair(nx, ny))
                current = Pair(nx, ny)
            } else {
                current = stack.removeAt(stack.size - 1)
            }
        }


        val exitSide = listOf("top", "bottom", "left", "right").random()
        var exitX = 0
        var exitY = 0

        when (exitSide) {
            "top" -> {
                exitX = 0
                exitY = (1 until cols - 1).random()
            }
            "bottom" -> {
                exitX = rows - 1
                exitY = (1 until cols - 1).random()
            }
            "left" -> {
                exitX = (1 until rows - 1).random()
                exitY = 0
            }
            "right" -> {
                exitX = (1 until rows - 1).random()
                exitY = cols - 1
            }
        }

        maze[exitX][exitY] = 0
        Log.d("exit", "$exitX, $exitY")
        exitX = when(exitX){
            0->  -1
            rows-1-> rows
            else-> exitX
        }
        exitY = when(exitY){
            0-> -1
            cols-1->cols
            else->exitY
        }
        return maze to Pair(exitX, exitY)
    }

}
private val levels= hashMapOf(
   1 to  MazeLevel(42,42,4,1),
   2 to  MazeLevel(21,21,3,2),
   3 to  MazeLevel(21,21,3,3),
)
private val defaultLevel = MazeLevel(21,21,3,-1)
