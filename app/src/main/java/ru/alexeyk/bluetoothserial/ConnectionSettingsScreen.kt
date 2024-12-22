package ru.alexeyk.bluetoothserial

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.alexeyk.bluetoothserial.ui.theme.BluetoothSerialTheme
import ru.alexeyk.bluetoothserial.viewmodel.SettingsViewModel

@Composable
fun ConnectionSettingsScreen(
    settingsViewModel: SettingsViewModel = viewModel()
) {

}

@Composable
fun Settings(){

}

@Preview(showBackground = true)
@Composable
fun PreviewSettings() {
    BluetoothSerialTheme {
       Settings()
    }
}