package ru.alexeyk.bluetoothserial

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.util.UUID

class ConnectThread(
    device: BluetoothDevice,
    val onConnect: () -> Unit,
    val onDisconnect: () -> Unit
) : Thread() {
    private val uuid = "00001101-0000-1000-8000-00805F9B34FB"
    private var socket: BluetoothSocket? = null

    init {
        try {
            socket = device.createRfcommSocketToServiceRecord(UUID.fromString(uuid))
        } catch (e: IOException) {
            Log.d("ConnectThread", "Socket's create() method failed ", e)
        } catch (se: SecurityException) {
            Log.d("ConnectThread", "Socket's create() method failed ", se)
        }
    }

    override fun run() {
        try {
            Log.d("ConnectThread", "Connecting ...")
            socket?.connect()
            Log.d("ConnectThread", "Connected")
            onConnect()
        } catch (e: IOException) {
            Log.d("ConnectThread", "Connection Lost ", e)
            onDisconnect()
        } catch (se: SecurityException) {
            Log.d("ConnectThread", "No Permission ", se)
        }
    }

    fun closeConnection() {
        try {
            socket?.close()
            Log.d("ConnectThread", "Closed")
            onDisconnect()
        } catch (e: IOException) {
            Log.d("ConnectThread", "closeConnection ", e)
        }
    }
}