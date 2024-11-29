package com.example.maze_game_app.domain

interface LevelRepository {
    fun getLevel():Int
    fun saveLevel(level: Int)
    fun resetLevel()
}