package com.example.bleservice.domain.repository

import com.example.bleservice.domain.model.Device

interface DeviceRepository {
    fun scanDevices(callback : ((List<Device>)->Unit))
    fun connectDevice(device: Device)
    fun disconnectDevice(device: Device)
}