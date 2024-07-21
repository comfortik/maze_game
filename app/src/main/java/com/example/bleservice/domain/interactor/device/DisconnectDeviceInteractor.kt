package com.example.bleservice.domain.interactor.device

import com.example.bleservice.domain.model.Device
import com.example.bleservice.domain.repository.DeviceRepository

class DisconnectDeviceInteractor(private val deviceRepository: DeviceRepository) {
    fun execute(device: Device){
        deviceRepository.disconnectDevice(device)
    }
}