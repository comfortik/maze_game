package com.example.bleservice.domain.interactor.device

import com.example.bleservice.domain.model.Device
import com.example.bleservice.domain.repository.DeviceRepository

class ScanDevicesInteractor(private val deviceRepository: DeviceRepository) {
    fun execute(callback: (List<Device>)->Unit){
        deviceRepository.scanDevices(callback)
    }
}