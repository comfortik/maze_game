package com.example.bleservice.data.repository

import com.example.bleservice.domain.interactor.dataPacket.DataPacketInteractors
import com.example.bleservice.domain.interactor.dataPacket.ReceiveDataPacketInteractor
import com.example.bleservice.domain.interactor.dataPacket.SendDataPacketInteractor
import javax.inject.Inject

class DataPacketInteractorsImpl @Inject constructor(
    private val sendDataPacketInteractor: SendDataPacketInteractor,
    private val receiveDataPacketInteractor: ReceiveDataPacketInteractor
) : DataPacketInteractors {
    override fun sendDataPacketInteractor() = sendDataPacketInteractor
    override fun receiveDataPacketInteractor() = receiveDataPacketInteractor
}
