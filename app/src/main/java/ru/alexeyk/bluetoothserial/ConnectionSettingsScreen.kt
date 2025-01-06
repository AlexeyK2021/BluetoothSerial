@file:OptIn(ExperimentalMaterial3Api::class)

package ru.alexeyk.bluetoothserial

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.graphics.Paint.Align
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.alexeyk.bluetoothserial.viewmodel.BluetoothConnectionState
import ru.alexeyk.bluetoothserial.viewmodel.BluetoothViewModel
import kotlin.enums.EnumEntries

@Composable
fun ConnectionSettingsScreen(bluetoothViewModel: BluetoothViewModel) {
    val devices = remember { bluetoothViewModel.getPairedDevices().toList() }
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(
                top = WindowInsets.systemBars
                    .asPaddingValues()
                    .calculateTopPadding(),
                bottom = WindowInsets.systemBars
                    .asPaddingValues()
                    .calculateBottomPadding(),
                start = 10.dp,
                end = 10.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val currConnection = bluetoothViewModel.currConnection
        val connect = bluetoothViewModel.currentState.observeAsState()
        BluetoothDevicesDropdown(
            initValue = if (currConnection.deviceMac.isNotEmpty()) "${currConnection.deviceName} (${currConnection.deviceMac})"
                        else "",
            items = devices,
            onSelectItem = { mac, name ->
                bluetoothViewModel.setMac(mac)
                bluetoothViewModel.setDeviceName(name)
            },
//             modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(25.dp))

        LineBreakDropDown(
            initValue = if (currConnection.deviceMac.isNotEmpty()) currConnection.lineBreak else LineBreak.CR_LF,
            items = LineBreak.entries,
            onSelectItem = { bluetoothViewModel.setLineBreak(it) },
//            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Row(modifier = Modifier.padding(top = 25.dp)) {
            Text(
                text = when (connect.value) {
                    BluetoothConnectionState.CONNECTED -> stringResource(R.string.device_connected)
                    BluetoothConnectionState.DISCONNECTED -> stringResource(R.string.device_disconnected)
                    else -> stringResource(R.string.connection_failed)
                }
            )

            Spacer(Modifier.width(20.dp))

            Icon(
                imageVector = when (connect.value) {
                    BluetoothConnectionState.CONNECTED -> Icons.Default.Check
                    else -> Icons.Default.Close
                },
                contentDescription = "Device connection status",
                modifier = Modifier.background(
                    color = when (connect.value) {
                        BluetoothConnectionState.CONNECTED -> Color.Green
                        else -> Color.Red
                    }
                )
            )
        }
        Button(
            onClick = {
                when (connect.value) {
                    BluetoothConnectionState.CONNECTED -> bluetoothViewModel.disconnect()
                    else -> bluetoothViewModel.connect()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            Text(
                text = when (connect.value) {
                    BluetoothConnectionState.CONNECTED -> stringResource(R.string.disconnect)
                    else -> stringResource(R.string.connect)
                }
            )
        }
    }
}

//enum class BaudRate(val value: Int) {
//    Baud1200(1200),
//    Baud2400(2400),
//    Baud4800(4800),
//    Baud9600(9600),
//    Baud14400(14400),
//    Baud19200(19200),
//    Baud38400(38400),
//    Baud57600(57600),
//    Baud115200(115200)
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun BaudRateDropDown(
//    onSelectItem: (BaudRate) -> Unit,
//    initValue: BaudRate,
//    items: EnumEntries<BaudRate>
//) {
//    var expanded by remember { mutableStateOf(false) }
//    var selectedOption by remember { mutableStateOf(initValue) }
//
//    ExposedDropdownMenuBox(
//        modifier = Modifier.width(150.dp),
//        expanded = expanded,
//        onExpandedChange = { expanded = !expanded },
//    ) {
//        TextField(
//            modifier = Modifier.menuAnchor(),
//            readOnly = true,
//            value = selectedOption.value.toString(),
//            onValueChange = {},
//            label = { Text(stringResource(R.string.baud_rate)) },
//            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
//            colors = ExposedDropdownMenuDefaults.textFieldColors(),
//        )
//        ExposedDropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false },
//        ) {
//            items.forEach {
//                DropdownMenuItem(
//                    text = { Text(it.value.toString()) },
//                    onClick = {
//                        selectedOption = it
//                        onSelectItem(it)
//                        expanded = false
//                    },
//                )
//            }
//        }
//    }
//}
//
//enum class StopBits(val value: Int) {
//    One(1),
//    Two(2)
//}

enum class LineBreak(val value: String) {
    NONE("None"),
    LF("LF"),
    CR("CR"),
    CR_LF("CR+LF")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LineBreakDropDown(
    onSelectItem: (LineBreak) -> Unit,
    initValue: LineBreak,
    items: EnumEntries<LineBreak>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(initValue) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        TextField(
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            value = selectedOption.value.toString(),
            onValueChange = {},
            label = { Text(stringResource(R.string.stop_bits)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            items.forEach {
                DropdownMenuItem(
                    text = { Text(it.value.toString()) },
                    onClick = {
                        selectedOption = it
                        onSelectItem(it)
                        expanded = false
                    },
                )
            }
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun BluetoothDevicesDropdown(
    onSelectItem: (String, String) -> Unit,
    initValue: String,
    items: List<BluetoothDevice>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(initValue) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        TextField(
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            value = selectedOption,
            onValueChange = {},
            label = { Text(stringResource(R.string.bluetooth_device_mac)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach {
                DropdownMenuItem(
                    text = { Text("${it.name} (${it.address})") },
                    onClick = {
                        selectedOption = "${it.name} (${it.address})"
                        onSelectItem(it.address, it.name)
                        expanded = false
                    },
                )
            }
        }
    }
}