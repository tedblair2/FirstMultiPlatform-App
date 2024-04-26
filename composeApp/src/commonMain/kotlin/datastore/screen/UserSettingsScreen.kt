package datastore.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import datastore.UserSettingsScreenActions
import datastore.UserSettingsScreenModel
import datastore.UserSettingsScreenState


class DataStoreExampleScreen:Screen{
    @Composable
    override fun Content() {
        val screenModel=getScreenModel<UserSettingsScreenModel>()
        val state by screenModel.state.collectAsState()

        UserSettingsScreen(
            userSettingsScreenState = state,
            onEvent = screenModel::onEvent
        )
    }
}

@Composable
fun UserSettingsScreen(
    userSettingsScreenState: UserSettingsScreenState,
    onEvent:(UserSettingsScreenActions)->Unit
) {
    Column(modifier = Modifier
        .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        Text(text = "Name is "+userSettingsScreenState.userSettings.name, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Address is "+userSettingsScreenState.userSettings.address, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Age is "+userSettingsScreenState.userSettings.age.toString(), fontSize = 24.sp)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = userSettingsScreenState.name,
            onValueChange = {
                onEvent(UserSettingsScreenActions.UpdateName(it))
            },
            label = {
                Text(text = "Enter name")
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = userSettingsScreenState.address,
            onValueChange = {
                onEvent(UserSettingsScreenActions.UpdateAddress(it))
            },
            label = {
                Text(text = "Enter address")
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = userSettingsScreenState.age,
            onValueChange = {
                onEvent(UserSettingsScreenActions.UpdateAge(it))
            },
            label = {
                Text(text = "Enter age")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {onEvent(UserSettingsScreenActions.AddUserSettings)},
            modifier = Modifier.padding(vertical = 5.dp, horizontal = 14.dp)){
            Text(text = "Save data")
        }
    }
}