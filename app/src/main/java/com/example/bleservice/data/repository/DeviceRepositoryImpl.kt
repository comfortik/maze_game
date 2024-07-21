package com.example.bleservice.data.repository

import com.example.bleservice.data.remote.BLEService
import com.example.bleservice.domain.model.Device
import com.example.bleservice.domain.repository.DeviceRepository
import javax.inject.Inject

class DeviceRepositoryImpl @Inject constructor(service :BLEService) : DeviceRepository {
    override fun scanDevices(callback: (List<Device>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun connectDevice(device: Device) {
        TODO("Not yet implemented")
    }

    override fun disconnectDevice(device: Device) {
        TODO("Not yet implemented")
    }
}