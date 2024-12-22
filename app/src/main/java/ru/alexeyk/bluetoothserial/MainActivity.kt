package ru.alexeyk.bluetoothserial

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.alexeyk.bluetoothserial.model.Message
import ru.alexeyk.bluetoothserial.ui.theme.BluetoothSerialTheme
import ru.alexeyk.bluetoothserial.viewmodel.SettingsViewModel
import java.sql.Time


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val msg = listOf(
            Message("Test1", Time(1734605402L), true),
            Message("Test2", Time(1734605402L), false)
        )
        val messagesLiveData: MutableLiveData<List<Message>> = MutableLiveData(msg)

        setContent {
            BluetoothSerialTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    MessagesScreen(
//                        messagesLiveData, onSendButtonClicked = {
//                            val newMessages = messagesLiveData.value!!.toMutableList()
//                            newMessages.add(Message(it, Time(System.currentTimeMillis()), false))
//                            messagesLiveData.value = newMessages
//                        },
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
                App()
            }
        }
    }
}

enum class Screens(@StringRes val title: Int, val icon: ImageVector) {
    MessagesScreen(R.string.messages_screen_title, Icons.AutoMirrored.Filled.Send),
    ConnectionSettingsScreen(R.string.connection_settings_screen_title, Icons.Filled.Settings);
}

@Composable
fun App(
    navController: NavHostController = rememberNavController(),
    settingsViewModel: SettingsViewModel = viewModel()
) {
    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                Screens.entries.forEach { screen ->
                    BottomNavigationItem(
                        icon = { Icon(imageVector = screen.icon, contentDescription = "") },
                        selected = currentDestination?.route == screen.name,
                        onClick = {
                            navController.navigate(screen.name) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
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
            composable(route = Screens.MessagesScreen.name) {
                MessagesScreen(
                    messagesList = /*messagesLiveData*/ MutableLiveData<List<Message>>(),
                    onSendButtonClicked = {})
            }

            composable(route = Screens.ConnectionSettingsScreen.name) {
                ConnectionSettingsScreen()
            }
        }

    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun Preview1() {
    BluetoothSerialTheme {
        App()
    }
}