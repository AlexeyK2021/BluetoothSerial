@file:OptIn(ExperimentalMaterial3Api::class)

package ru.alexeyk.bluetoothserial.screens

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.unit.sp
import ru.alexeyk.bluetoothserial.R
import ru.alexeyk.bluetoothserial.model.Connection
import ru.alexeyk.bluetoothserial.viewmodel.BluetoothConnectionState
import ru.alexeyk.bluetoothserial.viewmodel.BluetoothViewModel
import kotlin.enums.EnumEntries

@Composable
fun ConnectionSettingsScreen(bluetoothViewModel: BluetoothViewModel) {
//    val devices = remember { bluetoothViewModel.bluetoothDeviceSet.toList() }
    val devices = bluetoothViewModel.bluetoothDeviceSet.observeAsState().value?.toList()
    val currConnection = bluetoothViewModel.currConnection
    val connect = bluetoothViewModel.currentState.observeAsState()

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
//        BluetoothDevicesDropdown(
//            initValue = if (currConnection.deviceMac.isNotEmpty()) "${currConnection.deviceName} (${currConnection.deviceMac})"
//                        else "",
//            items = devices,
//            onSelectItem = { mac, name ->
//                bluetoothViewModel.setMac(mac)
//                bluetoothViewModel.setDeviceName(name)
//            },
//        )

//        Spacer(Modifier.height(25.dp))

        LineBreakDropDown(
            initValue = if (currConnection.deviceMac.isNotEmpty()) currConnection.lineBreak else LineBreak.CR_LF,
            items = LineBreak.entries,
            onSelectItem = { bluetoothViewModel.setLineBreak(it) },
        )

//        Row(modifier = Modifier.padding(top = 25.dp)) {
//            Text(
//                text = when (connect.value) {
//                    BluetoothConnectionState.CONNECTED -> stringResource(R.string.device_connected)
//                    BluetoothConnectionState.DISCONNECTED -> stringResource(R.string.device_disconnected)
//                    else -> stringResource(R.string.connection_failed)
//                }
//            )
//
//            Spacer(Modifier.width(20.dp))
//
//            Icon(
//                imageVector = when (connect.value) {
//                    BluetoothConnectionState.CONNECTED -> Icons.Default.Check
//                    else -> Icons.Default.Close
//                },
//                contentDescription = "Device connection status",
//                modifier = Modifier.background(
//                    color = when (connect.value) {
//                        BluetoothConnectionState.CONNECTED -> Color.Green
//                        else -> Color.Red
//                    }
//                )
//            )
//        }
//        Button(
//            onClick = {
//                when (connect.value) {
//                    BluetoothConnectionState.CONNECTED -> bluetoothViewModel.disconnect()
//                    else -> bluetoothViewModel.connect()
//                }
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 20.dp),
//            shape = RoundedCornerShape(10)
//        ) {
//            Text(
//                text = when (connect.value) {
//                    BluetoothConnectionState.CONNECTED -> stringResource(R.string.disconnect)
//                    else -> stringResource(R.string.connect)
//                }
//            )
//        }
        Spacer(Modifier.height(25.dp))
        BluetoothDevicesList(
            devices = devices ?: listOf(),
            currConnection,
            connect.value ?: BluetoothConnectionState.DISCONNECTED,
            onSelectItem = { address, name ->
                bluetoothViewModel.setMac(address)
                bluetoothViewModel.setDeviceName(name)
                bluetoothViewModel.connect()
            })
    }
}

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
            label = { Text(stringResource(R.string.line_break)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
//            colors = ExposedDropdownMenuDefaults.textFieldColors(),
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
//            colors = ExposedDropdownMenuDefaults.textFieldColors(),
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


@SuppressLint("MissingPermission")
@Composable
fun BluetoothDevicesList(
    devices: List<BluetoothDevice>,
    currentConnection: Connection,
    state: BluetoothConnectionState,
    onSelectItem: (String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        state = rememberLazyListState()
    ) {
        items(devices) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.15f), RoundedCornerShape(10))
                    .padding(start = 5.dp, end = 5.dp)
                    .clickable { onSelectItem(item.address, item.name) },
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(verticalArrangement = Arrangement.Center) {
                    Text(
                        text = item.name,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .align(Alignment.Start)
                    )
                    Text(
                        text = "MAC:\t" + item.address,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .align(Alignment.Start)
                    )
                }
                if (item.address == currentConnection.deviceMac && state != BluetoothConnectionState.DISCONNECTED) {
//                    Text(
//                        text = when (state) {
//                            BluetoothConnectionState.CONNECTED -> stringResource(R.string.device_connected)
//                            BluetoothConnectionState.DISCONNECTED -> stringResource(R.string.device_disconnected)
//                            else -> stringResource(R.string.connection_failed)
//                        },
//                        fontSize = 12.sp,
//                        modifier = Modifier.align(Alignment.CenterVertically)
//                    )
                    Icon(
                        imageVector = when (state) {
                            BluetoothConnectionState.CONNECTED -> Icons.Default.Done
                            else -> Icons.Default.Warning
                        },
                        contentDescription = "Device connection status",
                        tint = when (state) {
                            BluetoothConnectionState.CONNECTED -> Color.Green
                            else -> Color.Yellow
                        },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }
            Spacer(Modifier.height(15.dp))
        }
    }
}