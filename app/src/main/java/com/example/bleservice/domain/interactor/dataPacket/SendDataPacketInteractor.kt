package com.example.bleservice.domain.interactor.dataPacket

import com.example.bleservice.domain.model.DataPacket
import com.example.bleservice.domain.repository.DataPacketRepository

class SendDataPacketInteractor(private val dataPacketRepository: DataPacketRepository) {
    fun execute(dataPacket: DataPacket){
        dataPacketRepository.sendData(dataPacket)
    }
}