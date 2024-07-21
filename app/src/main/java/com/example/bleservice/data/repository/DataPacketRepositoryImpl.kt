package com.example.bleservice.data.repository

import com.example.bleservice.data.remote.BLEService
import com.example.bleservice.domain.model.DataPacket
import com.example.bleservice.domain.repository.DataPacketRepository
import javax.inject.Inject

class DataPacketRepositoryImpl @Inject constructor(
    service: BLEService
):DataPacketRepository {
    override fun sendData(dataPacket: DataPacket) {
        TODO("Not yet implemented")
    }

    override fun receiveData(): DataPacket {
        TODO("Not yet implemented")
    }

}