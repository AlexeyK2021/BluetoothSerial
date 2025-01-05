package ru.alexeyk.bluetoothserial.model

import ru.alexeyk.bluetoothserial.LineBreak


data class Connection(
//    var baudRate: BaudRate = BaudRate.Baud9600,
//    var stopBits: StopBits = StopBits.Two,
    var deviceMac: String = "",
    var deviceName: String = "",
    var lineBreak: LineBreak = LineBreak.CR_LF
)