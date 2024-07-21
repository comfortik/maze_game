package com.example.bleservice.data.remote

import android.Manifest
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class BLEService : Service() {

    private val binder = LocalBinder()
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private var scanCallback: ScanCallback? = null

    inner class LocalBinder : Binder() {
        fun getService(): BLEService = this@BLEService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        val bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
    }

    fun scanForDevices(callback: (ScanResult) -> Unit) {
        scanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                super.onScanResult(callbackType, result)
                callback(result)
            }

            override fun onBatchScanResults(results: List<ScanResult>) {
                super.onBatchScanResults(results)
            }

            override fun onScanFailed(errorCode: Int) {
                super.onScanFailed(errorCode)
                Log.e("BLEService", "Scan failed with error code: $errorCode")
            }
        }
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_SCAN
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            bluetoothAdapter.bluetoothLeScanner.startScan(scanCallback)
        } else {
            // Permission is not granted
            Log.e("BLEService", "Bluetooth scan permission is not granted")
            // Optionally handle permission request here
        }
    }

    fun stopScanning() {
        scanCallback?.let { callback ->
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_SCAN
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                bluetoothAdapter.bluetoothLeScanner.stopScan(callback)
            } else {
                Log.e("BLEService", "Bluetooth scan permission is not granted")
                // Optionally handle permission request here
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopScanning()
    }
}
