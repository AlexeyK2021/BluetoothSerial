package ru.alexeyk.bluetoothserial

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import ru.alexeyk.bluetoothserial.viewmodel.BluetoothConnectionState
import java.io.IOException
import java.util.UUID

class ConnectThread(
    device: BluetoothDevice,
    val lineBreak: LineBreak,
    val onUpdateState: (BluetoothConnectionState) -> Unit,
    val onReceiveMessage: (String) -> Unit
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
            onUpdateState(BluetoothConnectionState.CONNECTED)
            readMessage()
        } catch (e: IOException) {
            Log.d("ConnectThread", "Connection Lost ", e)
            onUpdateState(BluetoothConnectionState.FAILED_TO_CONNECT)
        } catch (se: SecurityException) {
            Log.d("ConnectThread", "No Permission ", se)
        }
    }

    fun closeConnection() {
        try {
            socket?.close()
            Log.d("ConnectThread", "Closed")
            onUpdateState(BluetoothConnectionState.DISCONNECTED)
        } catch (e: IOException) {
            Log.d("ConnectThread", "closeConnection ", e)
        }
    }

    fun sendMessage(msg: String) {
        try {
            socket?.outputStream?.write(msg.toByteArray())
        } catch (e: IOException) {
            Log.d("ConnectThread", "sendMessage ", e)
            onUpdateState(BluetoothConnectionState.FAILED_TO_SEND_MSG)
        }
    }

    private fun readMessage() {
        while (true) {
            try {
                val buffer = ByteArray(256)
//                val len = socket?.inputStream?.read(buffer)
//                val msg = String(buffer, 0, len ?: 0)
                val len = socket?.inputStream?.read(buffer) ?: 0
                Log.d("ConnectThread#readMessage", buffer.joinToString(";"))
                if(lineBreak == LineBreak.CR_LF && buffer[len-2] == 13.toByte() && buffer[len-1] == 10.toByte()){
                    val msg = String(buffer, 0, len - 2)
                    onReceiveMessage(msg)

                }else if(lineBreak == LineBreak.CR && buffer[len-1] == 13.toByte() ||
                    lineBreak == LineBreak.LF && buffer[len-1] == 10.toByte()){
                    val msg = String(buffer, 0, len - 1)
                    onReceiveMessage(msg)

                }else if(lineBreak == LineBreak.NONE){
                    val msg = String(buffer, 0, len)
                    onReceiveMessage(msg)
                }

            } catch (e: IOException) {
                Log.d("ConnectThread", "readMessage ", e)
                onUpdateState(BluetoothConnectionState.DISCONNECTED)
                break
            }
        }
    }
}