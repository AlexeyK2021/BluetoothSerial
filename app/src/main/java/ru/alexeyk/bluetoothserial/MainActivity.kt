package ru.alexeyk.bluetoothserial

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.alexeyk.bluetoothserial.ui.theme.BluetoothSerialTheme
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
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MessagesScreen(
                        messagesLiveData, onSendButtonClicked = {
                            val newMessages = messagesLiveData.value!!.toMutableList()
                            newMessages.add(Message(it, Time(System.currentTimeMillis()), false))
                            messagesLiveData.value = newMessages
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MessagesScreen(
    messagesList: LiveData<List<Message>>,
    onSendButtonClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        ShowMessages(messagesList)
        Spacer(modifier = modifier)
        TextPanel(onSendButtonClicked)
    }
}

@Composable
fun ShowMessages(messagesList: LiveData<List<Message>>) {
    val msgs = messagesList.observeAsState()
    LazyColumn(Modifier.fillMaxWidth()) {
        items(msgs.value!!) { msg ->
            Row {
                val dir = if (msg.rx) " <-- " else " --> "
                Text(text = msg.time.toString())
                Text(text = dir)
                Text(text = msg.text)
            }
        }
    }
}

@Composable
fun TextPanel(onSendButtonClicked: (String) -> Unit, modifier: Modifier = Modifier) {
    Row(modifier = modifier
        .fillMaxWidth()
        .height(50.dp)) {

        val textValue = remember{mutableStateOf("")}
        TextField(textValue.value,
            modifier = modifier.weight(0.75f).fillMaxHeight(),
            onValueChange = { textValue.value = it })

        Button(
            modifier = modifier.weight(0.25f).fillMaxHeight(),
            shape = RectangleShape, onClick = { onSendButtonClicked(textValue.value); textValue.value = "" },
        ) {
            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
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

//@Preview(showBackground = true)
//@Composable
//fun Preview1() {
//    BluetoothSerialTheme {
//        val innerPadding = 5.dp
//        MessagesScreen(
//            messagesList = listOf(
//                Message("Test1", Time(1734605402L), true),
//                Message("Test2", Time(1734605402L), false)
//            ),
//            onSendButtonClicked = {}
//        )
//    }
//}