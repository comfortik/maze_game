package com.example.bleservice.domain.interactor.dataPacket


interface DataPacketInteractors{
    fun sendDataPacketInteractor (): SendDataPacketInteractor
    fun receiveDataPacketInteractor(): ReceiveDataPacketInteractor
}