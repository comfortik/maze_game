package com.example.maze_game_app.di

import com.example.maze_game_app.maze.MazeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module


val koinModule = module{
    viewModel{MazeViewModel() }
}