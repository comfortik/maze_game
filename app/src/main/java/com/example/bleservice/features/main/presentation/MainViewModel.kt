package com.example.bleservice.features.main.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bleservice.domain.interactor.dataPacket.DataPacketInteractors
import com.example.bleservice.domain.interactor.device.DeviceInteractors
import com.example.bleservice.domain.model.Device
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataPacketInteractors: DataPacketInteractors,
    private val deviceInteractors: DeviceInteractors
): ViewModel()  {
    private val _devices = MutableLiveData<List<Device>>()
    val devices:LiveData<List<Device>> = _devices


    fun scanDevices(){
        deviceInteractors.scanDevicesInteractor().execute{
            _devices.value = it
        }
    }
    fun connectDevice(device: Device){
        deviceInteractors.connectDeviceInteractor().execute(device)
    }
    fun disconnectDevice(device: Device){
        deviceInteractors.disconnectDeviceInteractor().execute(device)
    }
}