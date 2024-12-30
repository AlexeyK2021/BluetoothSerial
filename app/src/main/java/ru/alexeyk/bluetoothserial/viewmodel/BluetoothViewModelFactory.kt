package ru.alexeyk.bluetoothserial.viewmodel

import android.app.Application
import android.bluetooth.BluetoothAdapter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class BluetoothViewModelFactory(private val btAdapter: BluetoothAdapter):
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        @Suppress("UNCHECKED_CAST")
        if(modelClass.isAssignableFrom(BluetoothViewModel::class.java))
            return BluetoothViewModel(btAdapter) as T
        else throw Exception("BluetoothViewModelFactory -> \'Error class\'")
    }
}
