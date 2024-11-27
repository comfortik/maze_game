package com.example.maze_game_app.maze.model

data class MazeUiState (
    val player: MazePlayer,
    val isWin: Boolean,
    val level: MazeLevel,
    val exitCoordinates: Pair<Int, Int>,
    val maze: Array<Array<Int>>
)