package com.example.bleservice.di

import com.example.bleservice.data.remote.BLEService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideBLEService(): BLEService {
        return BLEService()
    }
}
