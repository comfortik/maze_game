package com.example.bleservice.domain.interactor.dataPacket

import com.example.bleservice.domain.model.DataPacket
import com.example.bleservice.domain.repository.DataPacketRepository

class ReceiveDataPacketInteractor(private val dataPacketRepository: DataPacketRepository) {
    fun execute():DataPacket{
        return dataPacketRepository.receiveData()
    }
}