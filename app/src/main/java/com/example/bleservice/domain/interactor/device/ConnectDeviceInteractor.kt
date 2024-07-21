package com.example.bleservice.domain.interactor.device

import com.example.bleservice.domain.model.Device
import com.example.bleservice.domain.repository.DeviceRepository

class ConnectDeviceInteractor(private val deviceRepository: DeviceRepository) {
    fun execute(device: Device){
        deviceRepository.connectDevice(device)
    }
}