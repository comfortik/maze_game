package com.example.bleservice.data.repository

import com.example.bleservice.domain.interactor.device.ConnectDeviceInteractor
import com.example.bleservice.domain.interactor.device.DeviceInteractors
import com.example.bleservice.domain.interactor.device.DisconnectDeviceInteractor
import com.example.bleservice.domain.interactor.device.ScanDevicesInteractor
import javax.inject.Inject

class DeviceInteractorsImpl @Inject constructor(
    private val scanDevicesInteractor: ScanDevicesInteractor,
    private val connectDeviceInteractor: ConnectDeviceInteractor,
    private val disconnectDeviceInteractor: DisconnectDeviceInteractor
) : DeviceInteractors {
    override fun scanDevicesInteractor() = scanDevicesInteractor
    override fun connectDeviceInteractor() = connectDeviceInteractor
    override fun disconnectDeviceInteractor() = disconnectDeviceInteractor
}