package ru.alexeyk.bluetoothserial.model

data class Connection(
    var baudRate: Int = 115200,
    var stopBits: Int = 2,
    var deviceMac: String = ""
)