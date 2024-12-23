package ru.alexeyk.bluetoothserial

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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.alexeyk.bluetoothserial.ui.theme.BluetoothSerialTheme
import ru.alexeyk.bluetoothserial.viewmodel.SettingsViewModel
import kotlin.enums.EnumEntries


@Composable
fun ConnectionSettingsScreen(
) {
    Settings()
}

@Composable
fun Settings(settingsViewModel: SettingsViewModel = viewModel()) {
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
                start = 16.dp,
                end = 16.dp
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            BluetoothDevicesDropdown(
                initValue = "",
                items = listOf(""),
                onSelectItem = { settingsViewModel.setMac(it) })
        }
        Spacer(Modifier.height(25.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BaudRateDropDown(
                initValue = BaudRate.Baud9600,
                items = BaudRate.entries,
                onSelectItem = { settingsViewModel.setBaudRate(it) })

            StopBitsDropDown(
                initValue = StopBits.One,
                items = StopBits.entries,
                onSelectItem = { settingsViewModel.setStopBits(it) })
        }
    }
}

enum class BaudRate(val value: Int) {
    Baud1200(1200),
    Baud2400(2400),
    Baud4800(4800),
    Baud9600(9600),
    Baud14400(14400),
    Baud19200(19200),
    Baud38400(38400),
    Baud57600(57600),
    Baud115200(115200)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaudRateDropDown(
    onSelectItem: (BaudRate) -> Unit,
    initValue: BaudRate,
    items: EnumEntries<BaudRate>
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(initValue) }

    ExposedDropdownMenuBox(
        modifier = Modifier.width(150.dp),
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        TextField(
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            value = selectedOption.value.toString(),
            onValueChange = {},
            label = { Text(stringResource(R.string.baud_rate)) },
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

enum class StopBits(val value: Int) {
    One(1),
    Two(2)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StopBitsDropDown(
    onSelectItem: (StopBits) -> Unit,
    initValue: StopBits,
    items: EnumEntries<StopBits>
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(initValue) }

    ExposedDropdownMenuBox(
        modifier = Modifier.width(150.dp),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BluetoothDevicesDropdown(
    onSelectItem: (String) -> Unit,
    initValue: String,
    items: List<String>
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(initValue) }

    ExposedDropdownMenuBox(
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
            onDismissRequest = { expanded = false },
        ) {
            items.forEach {
                DropdownMenuItem(
                    text = { Text(it.toString()) },
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

@Preview(
    showBackground = true, showSystemUi = true,
    device = "spec:width=1080px,height=2340px,dpi=440,cutout=punch_hole,navigation=buttons"
)
@Composable
fun PreviewSettings() {
    BluetoothSerialTheme {
        Settings()
    }
}