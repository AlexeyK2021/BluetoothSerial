package ru.alexeyk.bluetoothserial.viewmodel

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.alexeyk.bluetoothserial.ConnectThread
import ru.alexeyk.bluetoothserial.LineBreak
import ru.alexeyk.bluetoothserial.model.Connection
import ru.alexeyk.bluetoothserial.model.Message
import java.sql.Time

enum class BluetoothConnectionState {
    DISCONNECTED,
    CONNECTED,
    FAILED_TO_CONNECT,
    FAILED_TO_SEND_MSG
}

class BluetoothViewModel(private val btAdapter: BluetoothAdapter) : ViewModel() {

    private val _messages: MutableLiveData<List<Message>> = MutableLiveData(listOf())
    val messages: MutableLiveData<List<Message>> get() = _messages

    private var currentConnection = Connection()
    val currConnection: Connection get() = currentConnection

    private var connectThread: ConnectThread? = null

    var currentState: MutableLiveData<BluetoothConnectionState> =
        MutableLiveData(BluetoothConnectionState.DISCONNECTED)

//    fun sendMessage(message: String) {
//        val msg = Message(message, Time(System.currentTimeMillis()), false)
//        _messages.value = _messages.value!!.plus(msg)
//    }

//    fun setBaudRate(baudRate: BaudRate) {
//        Log.d("BluetoothViewModel", "setBaudRate: $baudRate")
//        currentConnection.baudRate = baudRate
//    }

    fun setMac(mac: String) {
        Log.d("BluetoothViewModel", "setMAC: $mac")
        currentConnection.deviceMac = mac
    }

//    fun setStopBits(stopBits: StopBits) {
//        Log.d("BluetoothViewModel", "setStopBits: $stopBits")
//        currentConnection.stopBits = stopBits
//    }

    fun setLineBreak(lineBreak: LineBreak) {
        currentConnection.lineBreak = lineBreak
    }

    fun setDeviceName(name: String) {
        currentConnection.deviceName = name
    }

    fun connect() {
        if (btAdapter.isEnabled && currentConnection.deviceMac.isNotEmpty()) {
            Log.d("BluetoothViewModel", "connect: ${currentConnection.deviceMac}")
            val device = btAdapter.getRemoteDevice(currentConnection.deviceMac)
            connectThread = ConnectThread(
                device,
                currentConnection.lineBreak,
                onUpdateState = { btState -> currentState.postValue(btState) },
                onReceiveMessage = { msg ->
                    _messages.postValue(
                        _messages.value!!.plus(
                            Message(
                                msg,
                                Time(System.currentTimeMillis()),
                                true
                            )
                        )
                    )
                }
            )
            connectThread?.start()
        }
    }

    fun sendMessage(message: String) {
        val sb = StringBuilder(message)
        sb.append(
            when(currentConnection.lineBreak){
                LineBreak.CR_LF -> "\r\n"
                LineBreak.CR -> "\r"
                LineBreak.LF -> "\n"
                else -> ""
            }
        )
        connectThread?.sendMessage(sb.toString())
        val msg = Message(message, Time(System.currentTimeMillis()), false)
        _messages.value = _messages.value!!.plus(msg)
    }

    fun disconnect() {
        connectThread?.closeConnection()
    }

    @SuppressLint("MissingPermission")
    fun getPairedDevices(): Set<BluetoothDevice> {
        val pairedDevices: Set<BluetoothDevice> = btAdapter.bondedDevices as Set<BluetoothDevice>
        return pairedDevices
    }

    fun clearMessaged() {
        _messages.value = listOf()
    }

}