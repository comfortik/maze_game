package com.example.maze_game_app.presentation.maze.model

sealed interface MazeIntent {
    data object MoveTop: MazeIntent
    data object MoveDown: MazeIntent
    data object MoveRight: MazeIntent
    data object MoveLeft: MazeIntent
    data object StartGame: MazeIntent
    data object ResetProgress: MazeIntent
}