package ru.alexeyk.bluetoothserial

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.alexeyk.bluetoothserial.model.Message
import ru.alexeyk.bluetoothserial.viewmodel.MessagesViewModel


@Composable
fun MessagesScreen(
//    messagesList: MutableLiveData<List<Message>>,
//    onSendButtonClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    messagesViewModel: MessagesViewModel = viewModel(),
) {
    val msgs by  messagesViewModel.messages.observeAsState()
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        ShowMessages(msgs)
        Spacer(modifier = modifier)
        TextPanel(/*onSendButtonClicked*/ messagesViewModel)
    }
}

@Composable
fun ShowMessages(messagesList: List<Message>?) {
    LazyColumn(Modifier.fillMaxWidth()) {
        items(messagesList ?: emptyList()) { msg ->
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
fun TextPanel(/*onSendButtonClicked: (String) -> Unit,*/
              messagesViewModel: MessagesViewModel,
              modifier: Modifier = Modifier) {
    Row(modifier = modifier
        .fillMaxWidth()
        .height(50.dp)) {

        val textValue = remember{ mutableStateOf("") }
        TextField(textValue.value,
            modifier = modifier.weight(0.75f).fillMaxHeight(),
            onValueChange = { textValue.value = it })

        Button(
            modifier = modifier.weight(0.25f).fillMaxHeight(),
            shape = RectangleShape, onClick = { messagesViewModel.sendMessage(textValue.value); textValue.value = "" },
        ) {
            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
        }

    }
}