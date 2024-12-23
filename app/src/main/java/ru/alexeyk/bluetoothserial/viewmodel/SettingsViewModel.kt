package ru.alexeyk.bluetoothserial.viewmodel

import androidx.lifecycle.ViewModel
import ru.alexeyk.bluetoothserial.BaudRate
import ru.alexeyk.bluetoothserial.StopBits
import ru.alexeyk.bluetoothserial.model.Connection

class SettingsViewModel : ViewModel() {
    var currentConnection = Connection()

    fun setBaudRate(baudRate: BaudRate) {
        currentConnection.baudRate = baudRate
    }

    fun setMac(mac: String) {
        currentConnection.deviceMac = mac
    }

    fun setStopBits(stopBits: StopBits) {
        currentConnection.stopBits = stopBits
    }

}