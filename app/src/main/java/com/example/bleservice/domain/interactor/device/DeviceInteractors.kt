package com.example.bleservice.domain.interactor.device

interface DeviceInteractors {
    fun scanDevicesInteractor(): ScanDevicesInteractor
    fun connectDeviceInteractor(): ConnectDeviceInteractor
    fun disconnectDeviceInteractor(): DisconnectDeviceInteractor
}