package com.example.bleservice.domain.repository

import com.example.bleservice.domain.model.DataPacket

interface DataPacketRepository {
    fun sendData(dataPacket: DataPacket)
    fun receiveData(): DataPacket
}