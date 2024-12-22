package ru.alexeyk.bluetoothserial.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.alexeyk.bluetoothserial.model.Message
import java.sql.Time

class MessagesViewModel() : ViewModel() {
    private val _messages: MutableLiveData<List<Message>> = MutableLiveData(listOf())
    public val messages: MutableLiveData<List<Message>> get() = _messages

    fun sendMessage(message: String) {
        val msg = Message(message, Time(System.currentTimeMillis()), true)
        _messages.value = _messages.value!!.plus(msg)
    }

}