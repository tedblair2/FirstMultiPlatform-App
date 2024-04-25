package navigation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import event.ScreenAEvents
import navigation.ScreenAComponent

@Composable
fun ScreenAScreen(component:ScreenAComponent) {
    val text by component.text.subscribeAsState()

    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){

        Text(text = "Screen A", fontSize = 25.sp)
        OutlinedTextField(
            value = text,
            onValueChange = {
                component.onEvent(ScreenAEvents.EnterText(it))
            },
            label = {
                Text(text = "Enter value")
            },
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Button(onClick = {
            component.onEvent(ScreenAEvents.ClickButton)
        }){
            Text(text = "Go to screen B")
        }
    }
}