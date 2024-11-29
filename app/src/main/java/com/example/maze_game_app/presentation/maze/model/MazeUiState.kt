package com.example.maze_game_app.presentation.maze.model

data class MazeUiState (
    val player: MazePlayer = initialPlayer ,
    val isWin: Boolean = false  ,
    val level: MazeLevel = initialLevel,
    val exitCoordinates: Pair<Int, Int> = initialExitCoordinates,
    val maze: Array<Array<Int>> = arrayOf()
)
private val initialLevel = MazeLevel(20,20,5,1)
private val initialPlayer = MazePlayer(10,10)
private val initialExitCoordinates = Pair(0,0)
