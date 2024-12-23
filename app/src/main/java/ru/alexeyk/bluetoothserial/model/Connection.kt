package ru.alexeyk.bluetoothserial.model

import ru.alexeyk.bluetoothserial.BaudRate
import ru.alexeyk.bluetoothserial.StopBits

data class Connection(
    var baudRate: BaudRate = BaudRate.Baud9600,
    var stopBits: StopBits = StopBits.Two,
    var deviceMac: String = ""
)