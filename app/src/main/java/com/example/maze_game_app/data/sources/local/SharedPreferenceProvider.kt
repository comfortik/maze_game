package com.example.maze_game_app.data.sources.local

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object SharedPreferenceProvider {
    private const val TAG = "SHARED PREFERENCES PROVIDER"
    private lateinit var sharedPrefs: SharedPreferences
    fun init(context: Context){
        if(!::sharedPrefs.isInitialized){
            sharedPrefs = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        }
    }
    fun <T> saveValue(key: String, value: T) {
        try{
            with(sharedPrefs.edit()) {
                when (value) {
                    is String -> putString(key, value)
                    is Int -> putInt(key, value)
                    is Boolean -> putBoolean(key, value)
                    is Float -> putFloat(key, value)
                    is Long -> putLong(key, value)
                    is Set<*> -> putStringSet(key, value.filterIsInstance<String>().toSet())
                    else -> throw IllegalArgumentException("Unsupported type")
                }
                apply()
            }
        }catch (e: Exception){
            Log.e(TAG, e.message.toString())
        }
    }

    fun <T> getValue(key: String, defaultValue: T): T {
        try{
            return when (defaultValue) {
                is String -> sharedPrefs.getString(key, defaultValue) as T
                is Int -> sharedPrefs.getInt(key, defaultValue) as T
                is Boolean -> sharedPrefs.getBoolean(key, defaultValue) as T
                is Float -> sharedPrefs.getFloat(key, defaultValue) as T
                is Long -> sharedPrefs.getLong(key, defaultValue) as T
                is Set<*> -> sharedPrefs.getStringSet(key, defaultValue as? Set<String>) as T
                else -> throw IllegalArgumentException("Unsupported type")
            }
        }catch (e: Exception){
            Log.e(TAG, e.message.toString())
        }
        return defaultValue
    }



}