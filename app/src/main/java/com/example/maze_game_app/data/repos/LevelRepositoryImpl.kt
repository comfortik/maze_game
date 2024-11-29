package com.example.maze_game_app.data.repos

import com.example.maze_game_app.data.sources.local.SharedPreferenceProvider
import com.example.maze_game_app.domain.LevelRepository

class LevelRepositoryImpl: LevelRepository {
    companion object{
        private const val LEVEL_KEY = "LEVEL_KEY"
        private const val INITIAL_LEVEL = 1
    }
    override fun getLevel(): Int =
        SharedPreferenceProvider.getValue(LEVEL_KEY, 1)



    override fun saveLevel(level: Int) {
        SharedPreferenceProvider.saveValue(LEVEL_KEY, level )
    }

    override fun resetLevel() {
        SharedPreferenceProvider.saveValue(LEVEL_KEY, INITIAL_LEVEL)
    }
}