package ru.alexeyk.bluetoothserial.model

import java.sql.Time

data class Message(
    val text: String,
    val time: Time,
    val rx: Boolean
)
