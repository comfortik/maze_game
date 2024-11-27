package com.example.maze_game_app.maze.model

sealed interface MazeActions {
    data class ShowWinAlert(val message: String): MazeActions

}