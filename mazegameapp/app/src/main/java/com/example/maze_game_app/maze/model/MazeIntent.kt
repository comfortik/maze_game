package com.example.maze_game_app.maze.model

sealed interface MazeIntent {
    data object MoveTop: MazeIntent
    data object MoveDown: MazeIntent
    data object MoveRight: MazeIntent
    data object MoveLeft: MazeIntent
    data object StartGame: MazeIntent
}