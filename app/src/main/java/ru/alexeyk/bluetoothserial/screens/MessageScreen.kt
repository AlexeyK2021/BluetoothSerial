package ru.alexeyk.bluetoothserial.screens

import android.annotation.SuppressLint
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import ru.alexeyk.bluetoothserial.model.Message
import ru.alexeyk.bluetoothserial.viewmodel.BluetoothViewModel


@Composable
fun MessagesScreen(
    bluetoothViewModel: BluetoothViewModel,
    modifier: Modifier = Modifier,
) {
    val msgs by bluetoothViewModel.messages.observeAsState()
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        ShowMessages(msgs, modifier.fillMaxWidth().weight(1f, fill = false))
        Spacer(modifier = modifier)
        TextPanel(
            onSendButtonClicked = { bluetoothViewModel.sendMessage(it) },
            onClearChatClicked = { bluetoothViewModel.clearMessaged() }
        )
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ShowMessages(messagesList: List<Message>?, modifier: Modifier = Modifier) {
    val lazyListState = rememberLazyListState()
    val cs = rememberCoroutineScope()

    cs.launch {
        val index = if (messagesList.isNullOrEmpty()) 0 else messagesList.size.minus(1)
        lazyListState.scrollToItem(index)
    }

    LazyColumn(modifier = modifier, state = lazyListState) {
        items(messagesList ?: emptyList()) { msg ->
            Row {
                val dir = if (msg.rx) " <-- " else " --> "
                Text(text = msg.time.toString())
                Text(text = dir)
                Text(text = msg.text)
            }
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}

@Composable
fun TextPanel(
    onSendButtonClicked: (String) -> Unit,
    onClearChatClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {

//        Button(
//            modifier = modifier
//                .weight(0.25f)
//                .fillMaxHeight(),
//            shape = RectangleShape, onClick = onClearChatClicked
//        ) {
//            Icon(Icons.Default.Delete, contentDescription = "Clear chat")
//        }
        val textValue = remember { mutableStateOf("") }
        TextField(textValue.value,
            modifier = modifier
                .weight(0.75f)
                .fillMaxHeight(),
            onValueChange = { textValue.value = it }, )

        Button(
            modifier = modifier
                .weight(0.15f)
                .fillMaxHeight(),
            shape = RoundedCornerShape(10),
            onClick = { onSendButtonClicked(textValue.value); textValue.value = "" },
        ) {
            Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Send")
        }

    }
}