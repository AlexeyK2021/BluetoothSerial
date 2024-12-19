package ru.alexeyk.bluetoothserial

import java.sql.Time

data class Message(
    val text: String,
    val time: Time,
    val rx: Boolean
)
