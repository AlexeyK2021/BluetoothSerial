package ru.alexeyk.bluetoothserial.viewmodel

import androidx.lifecycle.ViewModel
import ru.alexeyk.bluetoothserial.model.Connection

class SettingsViewModel : ViewModel() {
    var currentConnection = Connection()

    fun setBaudRate(baudRate: Int) {
        currentConnection.baudRate = baudRate
    }

    fun setMac(mac: String) {
        currentConnection.deviceMac = mac
    }

    fun setStopBits(stopBits: Int) {
        currentConnection.stopBits = stopBits
    }
}