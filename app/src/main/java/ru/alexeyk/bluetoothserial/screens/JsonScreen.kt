package ru.alexeyk.bluetoothserial.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getString
import org.json.JSONObject
import ru.alexeyk.bluetoothserial.R
import ru.alexeyk.bluetoothserial.model.JsonItem
import ru.alexeyk.bluetoothserial.viewmodel.BluetoothViewModel
import java.text.SimpleDateFormat

@Composable
fun JsonScreen(btViewModel: BluetoothViewModel) {
//    TODO("Not yet implemented")
    val msgs = btViewModel.messages.observeAsState()
    val jsonMap = mutableListOf<JsonItem>()
    val sdf = SimpleDateFormat("HH:mm:ss")
    var errorParse = false

    try {
        val json = JSONObject(msgs.value!!.last().text)
        for (key in json.keys()) {
            Log.d("JsonScreen", "JSON(${key})=${json[key]}")
            jsonMap.add(JsonItem(key, json[key].toString()))
        }
        errorParse = false

    } catch (e: Exception) {
        Log.d("JsonScreen", e.toString())
        errorParse = true
    }
    if (msgs.value!!.isNotEmpty() && !errorParse) {
        Column(modifier = Modifier.padding()) {
            Text(text = stringResource(R.string.message_time) + ": " + msgs.value?.last()?.time?.let {
                sdf.format(it)
            }, fontSize = 18.sp, modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))
            Spacer(modifier = Modifier.padding(top = 5.dp, bottom = 5.dp))
            LazyColumn {
                items(jsonMap) {
                    ListItem(it.key, it.value)
                }
            }
        }
    }else if(errorParse){
        Text(text = stringResource(R.string.message_parse_error),
            fontSize = 18.sp, modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))
    }
}

@Composable
fun ListItem(key: String, value: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = key)
        Text(text = value)
    }
//    Spacer(modifier.padding(top = 5.dp))
    HorizontalDivider(thickness = 1.dp, color = Color.Gray)
    Spacer(modifier.padding(top = 7.dp))

}