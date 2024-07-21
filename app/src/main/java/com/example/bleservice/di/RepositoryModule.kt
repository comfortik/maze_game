package com.example.bleservice.di

import com.example.bleservice.data.remote.BLEService
import com.example.bleservice.data.repository.DataPacketRepositoryImpl
import com.example.bleservice.data.repository.DeviceRepositoryImpl
import com.example.bleservice.domain.repository.DataPacketRepository
import com.example.bleservice.domain.repository.DeviceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideDataPacketRepository(
        bleService: BLEService
    ): DataPacketRepository {
        return DataPacketRepositoryImpl(bleService)
    }

    @Provides
    @Singleton
    fun provideDeviceRepository(
        bleService: BLEService
    ): DeviceRepository {
        return DeviceRepositoryImpl(bleService)
    }
}
