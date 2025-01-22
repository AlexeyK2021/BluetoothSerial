package ru.alexeyk.bluetoothserial

import android.Manifest.permission.BLUETOOTH
import android.Manifest.permission.BLUETOOTH_CONNECT
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.sharp.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppTheme
import ru.alexeyk.bluetoothserial.screens.ConnectionSettingsScreen
import ru.alexeyk.bluetoothserial.screens.MessagesScreen
import ru.alexeyk.bluetoothserial.viewmodel.BluetoothViewModel
import ru.alexeyk.bluetoothserial.viewmodel.BluetoothViewModelFactory


class MainActivity : ComponentActivity() {
    private var btAdapter: BluetoothAdapter? = null
    private lateinit var btLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestPermission()
        registerBtLauncher()
        initBtAdapter()

        val viewModel: BluetoothViewModel by viewModels { BluetoothViewModelFactory(btAdapter!!) }
        setContent {
            AppTheme {
                App(viewModel)
            }
        }
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(this, BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(BLUETOOTH, BLUETOOTH_CONNECT), 0)
        }
    }

    private fun registerBtLauncher() {
        btLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                Toast.makeText(this, getString(R.string.bluetooth_is_enabled), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, getString(R.string.bluetooth_is_not_enabled), Toast.LENGTH_SHORT).show()
                btLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
            }
        }
    }

    private fun initBtAdapter() {
        val btManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        btAdapter = btManager.adapter

        if (btAdapter == null) {
            Toast.makeText(this, getString(R.string.bluetooth_is_not_available), Toast.LENGTH_SHORT).show()
        } else if (!btAdapter!!.isEnabled) {
            Toast.makeText(this, getString(R.string.bluetooth_is_not_enabled), Toast.LENGTH_SHORT).show()
            btLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
        }
    }
}

enum class Screens(@StringRes val title: Int, val icon: ImageVector) {
    MessagesScreen(R.string.messages_screen_title, Icons.Rounded.Email),
//    JsonScreen(R.string.json_screen_title, Icons.Filled.Menu),
    ConnectionSettingsScreen(R.string.connection_settings_screen_title, Icons.Rounded.Settings)
}

@Composable
fun App(
    btViewModel: BluetoothViewModel,
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        bottomBar = {
            NavigationBar(
//                containerColor = ,
//                contentColor = Color.Gray
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                Screens.entries.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(imageVector = screen.icon, contentDescription = "") },
                        label = {
                            Text(
                                text = stringResource(screen.title),
                                fontSize = 10.sp
                            )
                        },
                        selected = currentDestination?.route.equals(screen.name),
                        onClick = {
                            navController.navigate(screen.name) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
//                        colors = NavigationBarItemDefaults.colors(
//                            selectedIconColor = Color.LightGray,
//                            selectedTextColor = Color.LightGray,
//                            unselectedIconColor = Color.Gray,
//                            unselectedTextColor = Color.Gray
//                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screens.MessagesScreen.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Screens.MessagesScreen.name) { MessagesScreen(btViewModel) }
//            composable(route = ru.alexeyk.bluetoothserial.Screens.JsonScreen.name){ JsonScreen(btViewModel) }
            composable(route = Screens.ConnectionSettingsScreen.name) { ConnectionSettingsScreen(btViewModel) }
        }

    }
}
