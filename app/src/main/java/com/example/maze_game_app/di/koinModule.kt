package com.example.maze_game_app.di

import com.example.maze_game_app.data.repos.LevelRepositoryImpl
import com.example.maze_game_app.domain.LevelRepository
import com.example.maze_game_app.presentation.maze.MazeViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module


val koinModule = module{
    single<LevelRepository> { LevelRepositoryImpl() }
    viewModelOf(::MazeViewModel)
}