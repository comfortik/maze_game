package com.example.maze_game_app.presentation.maze

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.maze_game_app.data.sources.local.SharedPreferenceProvider
import com.example.maze_game_app.domain.LevelRepository
import com.example.maze_game_app.presentation.maze.model.MazeActions
import com.example.maze_game_app.presentation.maze.model.MazeIntent
import com.example.maze_game_app.presentation.maze.model.MazeLevel
import com.example.maze_game_app.presentation.maze.model.MazePlayer
import com.example.maze_game_app.presentation.maze.model.MazeUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class MazeViewModel(
    private val levelRepository: LevelRepository
): ViewModel() {

    companion object{
        private const val TAG = "MAZE VIEW MODEL"
    }

    private val _state = MutableStateFlow(MazeUiState())
    val state = _state.asStateFlow()

    private val _actions = MutableSharedFlow<MazeActions>(replay = 1)
    val actions = _actions.asSharedFlow()

    init {
        initLevel()
    }

    private fun initLevel(){
        try{
            val level = levelRepository.getLevel()
            val row = levels[level]!!.rows
            val (firstMaze, exits) = generateMaze(row, row)
            _state.value = MazeUiState(
                player = MazePlayer(row / 2, row / 2),
                isWin = false,
                level = levels[level]?: defaultLevel,
                exitCoordinates = exits,
                maze = firstMaze
            )
        }catch (e: Exception){
            Log.e(TAG, e.message.toString())
        }
    }

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
                startNextLevel()
            }
            MazeIntent.ResetProgress -> {
                levelRepository.resetLevel()
                initLevel()
            }
        }
    }
    private fun startNextLevel(){
        val newLevel = if(state.value.level.num< levels.size){
            levels[state.value.level.num+1]
        }else{
            levels[state.value.level.num]
        }
        levelRepository.saveLevel(newLevel?.num?:1)

        val newRows = newLevel?.rows?:20
        val newCols = newLevel?.cols?:20

        val (maze, exitC) = generateMaze(newRows, newCols)
        _state.value = MazeUiState(
            player = MazePlayer(newRows/2, newCols/2),
            isWin = false,
            level = newLevel?: defaultLevel,
            exitCoordinates = exitC,
            maze = maze
        )
    }
    private  fun movePlayer(player: MazePlayer, maze: Array<Array<Int>>, dx: Int, dy: Int, exitCoordinates: Pair<Int, Int>) {
        val newX = player.x + dx
        val newY = player.y + dy
        if(exitCoordinates.first==newX && exitCoordinates.second==newY){
            _state.value = state.value.copy(
                player = MazePlayer(newX, newY)
            )
            val emitted = _actions.tryEmit(MazeActions.ShowWinAlert("You win in ${state.value.level}"))
            Log.d(TAG, "Success emitted: $emitted")

        }
        else if (newX in maze.indices && newY in maze[0].indices && maze[newX][newY] == 0) {

            _state.value = state.value.copy(
                player = MazePlayer(newX, newY)
            )
            Log.d(TAG, "New player position: $newX, $newY")
        }

        else {
            Log.d(TAG, "Blocked position: $newX, $newY")
        }
    }
    private fun generateMaze(rows: Int, cols: Int): Pair<Array<Array<Int>>, Pair<Int, Int> >{
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
            rows-> rows-1
            else-> exitX
        }
        exitY = when(exitY){
            0-> -1
            cols->cols-1
            else->exitY
        }
        return maze to Pair(exitX, exitY)
    }

}
private val levels= hashMapOf(
   1 to  MazeLevel(20,20,9,1),
   2 to  MazeLevel(20,20,9,2),
   3 to  MazeLevel(20,20,8,3),
   4 to  MazeLevel(20,20,8,4),
   5 to  MazeLevel(20,20,8,5),
   6 to  MazeLevel(20,20,8,6),
   7 to  MazeLevel(24,24,10,7),
   8 to  MazeLevel(24,24,9,8),
   9 to  MazeLevel(24,24,9,9),
   10 to  MazeLevel(24,24,8,10),
   11 to  MazeLevel(24,24,8,11),
   12 to  MazeLevel(24,24,8,12),
   13 to  MazeLevel(24,24,8,13),
    14 to  MazeLevel(28,28,10,14),
    15 to  MazeLevel(28,28,10,15),
    16 to  MazeLevel(28,28,9,16),
    17 to  MazeLevel(28,28,9,17),
    18 to  MazeLevel(28,28,9,18),
    19 to  MazeLevel(28,28,8,19),
    20 to  MazeLevel(32,32,8,20),
    21 to  MazeLevel(32,32,8,21),
    22 to  MazeLevel(32,32,8,22),
    23 to  MazeLevel(32,32,8,23),
    24 to  MazeLevel(32,32,8,24),
    25 to  MazeLevel(32,32,8,25),
    26 to  MazeLevel(32,32,7,26),
    27 to  MazeLevel(32,32,7,27),
    28 to  MazeLevel(36,36,7,28),
    29 to  MazeLevel(36,36,7,29),
    30 to  MazeLevel(36,36,7,30),
    31 to  MazeLevel(36,36,6,31),
    32 to  MazeLevel(36,36,6,32),
    33 to  MazeLevel(36,36,6,33),
    34 to  MazeLevel(36,36,6,34),
    35 to  MazeLevel(40,40,10,35),
    36 to  MazeLevel(40,40,10,36),
    37 to  MazeLevel(40,40,9,37),
    38 to  MazeLevel(40,40,9,38),
    39 to  MazeLevel(40,40,9,39),
    40 to  MazeLevel(44,44,8,40),
    41 to  MazeLevel(44,44,8,41),
    42 to  MazeLevel(44,44,8,42),
    43 to  MazeLevel(44,44,7,43),
    44 to  MazeLevel(44,44,7,44),
    45 to  MazeLevel(44,44,7,45),
    46 to  MazeLevel(44,44,7,46),
    47 to  MazeLevel(44,44,7,47),
    48 to  MazeLevel(48,48,7,48),
    49 to  MazeLevel(48,48,6,49),
    50 to  MazeLevel(48,48,6,50),
    51 to  MazeLevel(48,48,6,51),
    52 to  MazeLevel(48,48,6,52),
    53 to  MazeLevel(48,48,6,53),
    54 to  MazeLevel(48,48,6,54),
    55 to  MazeLevel(48,48,6,55),
    56 to  MazeLevel(52,52,5,56),
    57 to  MazeLevel(52,52,5,57),
    58 to  MazeLevel(52,52,5,58),
    59 to  MazeLevel(52,52,5,59),
    60 to  MazeLevel(52,52,5,60),

)


private val defaultLevel = MazeLevel(20,20,5,-1)
